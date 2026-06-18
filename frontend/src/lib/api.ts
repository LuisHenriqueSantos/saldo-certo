import type { ApiError } from "@/lib/types";

const API_URL = (process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api/v1").replace(/\/$/, "");
const NETWORK_ERROR_MESSAGE = "Nao foi possivel conectar com a API. Verifique se o backend esta rodando.";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const normalizedPath = path.startsWith("/") ? path : `/${path}`;
  let response: Response;

  try {
    response = await fetch(`${API_URL}${normalizedPath}`, {
      ...init,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
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
    throw new Error(`${error.message ?? "Erro na requisicao."}${details}`);
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
