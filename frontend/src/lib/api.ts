import type { ApiError } from "@/lib/types";

const API_URL = (process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api/v1").replace(/\/$/, "");
const NETWORK_ERROR_MESSAGE = "Nao foi possivel conectar com a API. Verifique se o backend esta rodando.";
const TOKEN_KEY = "meuSaldoMensal.accessToken";
const USER_KEY = "meuSaldoMensal.user";
const TENANT_KEY = "meuSaldoMensal.tenant";
const SESSION_MESSAGE_KEY = "meuSaldoMensal.sessionMessage";

export function getAccessToken() {
  if (typeof window === "undefined") {
    return null;
  }
  return window.localStorage.getItem(TOKEN_KEY);
}

export function hasSession() {
  return Boolean(getAccessToken());
}

export function saveSession(session: { accessToken: string; user: unknown; tenant: unknown }) {
  if (typeof window === "undefined") {
    return;
  }
  window.localStorage.setItem(TOKEN_KEY, session.accessToken);
  window.localStorage.setItem(USER_KEY, JSON.stringify(session.user));
  window.localStorage.setItem(TENANT_KEY, JSON.stringify(session.tenant));
}

export function clearSession(message?: string) {
  if (typeof window === "undefined") {
    return;
  }
  window.localStorage.removeItem(TOKEN_KEY);
  window.localStorage.removeItem(USER_KEY);
  window.localStorage.removeItem(TENANT_KEY);
  if (message) {
    window.sessionStorage.setItem(SESSION_MESSAGE_KEY, message);
  }
}

export function consumeSessionMessage() {
  if (typeof window === "undefined") {
    return null;
  }
  const message = window.sessionStorage.getItem(SESSION_MESSAGE_KEY);
  if (message) {
    window.sessionStorage.removeItem(SESSION_MESSAGE_KEY);
  }
  return message;
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const normalizedPath = path.startsWith("/") ? path : `/${path}`;
  const token = getAccessToken();
  const isPublicAuth = normalizedPath === "/auth/login" || normalizedPath === "/auth/register";
  let response: Response;

  try {
    response = await fetch(`${API_URL}${normalizedPath}`, {
      ...init,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...(token && !isPublicAuth ? { Authorization: `Bearer ${token}` } : {}),
        ...(init?.headers ?? {})
      }
    });
  } catch (error) {
    console.error("API request failed", {
      url: `${API_URL}${normalizedPath}`,
      error
    });
    throw new Error(NETWORK_ERROR_MESSAGE);
  }

  if (!response.ok) {
    let error: ApiError = {};
    try {
      error = await response.json();
    } catch {
      error = { message: "Erro inesperado na requisicao." };
    }
    const details = error.details?.length ? ` ${error.details.join(" ")}` : "";
    console.error("API response error", {
      url: `${API_URL}${normalizedPath}`,
      status: response.status,
      error
      });
    const message = `${error.message ?? "Erro na requisicao."}${details}`;
    if (response.status === 401 && normalizedPath !== "/auth/login") {
      const expiredMessage = "Sua sessão expirou. Faça login novamente.";
      clearSession(expiredMessage);
      if (typeof window !== "undefined" && window.location.pathname !== "/") {
        window.location.assign("/");
      }
      throw new Error(expiredMessage);
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const api = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, body: unknown) =>
    request<T>(path, { method: "POST", body: JSON.stringify(body) }),
  put: <T>(path: string, body: unknown) =>
    request<T>(path, { method: "PUT", body: JSON.stringify(body) }),
  patch: <T>(path: string, body?: unknown) =>
    request<T>(path, { method: "PATCH", body: body ? JSON.stringify(body) : undefined }),
  delete: <T>(path: string) => request<T>(path, { method: "DELETE" })
};

export function queryMonthYear(month: number, year: number) {
  return `month=${month}&year=${year}`;
}
