"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { ArrowRight, Loader2 } from "lucide-react";
import { PasswordInput } from "@/components/auth/PasswordInput";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { api, saveSession } from "@/lib/api";
import type { LoginResponse } from "@/lib/types";

type LoginFormProps = {
  onCreateAccount: () => void;
};

type LoginErrors = {
  email?: string;
  password?: string;
  form?: string;
};

export function LoginForm({ onCreateAccount }: LoginFormProps) {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<LoginErrors>({});

  async function submit(event: FormEvent) {
    event.preventDefault();
    const nextErrors: LoginErrors = {};
    if (!email.trim()) {
      nextErrors.email = "E-mail é obrigatório.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      nextErrors.email = "Informe um e-mail válido.";
    }
    if (!password) {
      nextErrors.password = "Senha é obrigatória.";
    }
    setErrors(nextErrors);
    if (Object.keys(nextErrors).length) {
      return;
    }

    setLoading(true);
    try {
      const session = await api.post<LoginResponse>("/auth/login", { email, password });
      saveSession(session);
      router.push("/dashboard");
    } catch (error) {
      const message = (error as Error).message;
      setErrors({ form: message.includes("senha") ? "E-mail ou senha inválidos." : message || "E-mail ou senha inválidos." });
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={submit} className="space-y-4">
      {errors.form ? (
        <div className="rounded-xl border border-red-500/25 bg-red-500/10 px-4 py-3 text-sm font-medium text-red-100">
          {errors.form}
        </div>
      ) : null}

      <Input
        label="E-mail"
        name="email"
        type="email"
        value={email}
        autoComplete="email"
        placeholder="voce@email.com"
        error={errors.email}
        onChange={(event) => setEmail(event.target.value)}
      />
      <PasswordInput label="Senha" name="password" value={password} autoComplete="current-password" error={errors.password} onChange={setPassword} />

      <Button type="submit" className="h-11 w-full" disabled={loading}>
        {loading ? <Loader2 className="h-4 w-4 animate-spin" /> : <ArrowRight className="h-4 w-4" />}
        Entrar
      </Button>

      <button type="button" className="w-full text-sm font-bold text-orange-300 transition hover:text-orange-200" onClick={onCreateAccount}>
        Criar conta
      </button>
    </form>
  );
}
