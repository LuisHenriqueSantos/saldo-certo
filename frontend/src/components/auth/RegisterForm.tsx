"use client";

import { FormEvent, useState } from "react";
import { Loader2, UserPlus } from "lucide-react";
import { PasswordInput } from "@/components/auth/PasswordInput";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { api } from "@/lib/api";

type RegisterFormProps = {
  onBackToLogin: (message?: string) => void;
};

type RegisterErrors = {
  name?: string;
  email?: string;
  password?: string;
  confirmPassword?: string;
  accountName?: string;
  form?: string;
};

export function RegisterForm({ onBackToLogin }: RegisterFormProps) {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    accountName: ""
  });
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<RegisterErrors>({});

  function update(field: keyof typeof form, value: string) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function submit(event: FormEvent) {
    event.preventDefault();
    const nextErrors: RegisterErrors = {};
    if (!form.name.trim()) {
      nextErrors.name = "Nome é obrigatório.";
    }
    if (!form.email.trim()) {
      nextErrors.email = "E-mail é obrigatório.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      nextErrors.email = "Informe um e-mail válido.";
    }
    if (!form.password) {
      nextErrors.password = "Senha é obrigatória.";
    } else if (form.password.length < 8) {
      nextErrors.password = "Use pelo menos 8 caracteres.";
    }
    if (!form.confirmPassword) {
      nextErrors.confirmPassword = "Confirme sua senha.";
    } else if (form.password !== form.confirmPassword) {
      nextErrors.confirmPassword = "As senhas não conferem.";
    }
    if (!form.accountName.trim()) {
      nextErrors.accountName = "Nome da conta financeira é obrigatório.";
    }
    setErrors(nextErrors);
    if (Object.keys(nextErrors).length) {
      return;
    }

    setLoading(true);
    try {
      await api.post("/auth/register", form);
      onBackToLogin("Conta criada com sucesso. Agora você já pode acessar o sistema.");
    } catch (error) {
      setErrors({ form: (error as Error).message });
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

      <Input label="Nome" name="name" value={form.name} placeholder="Luis Henrique" error={errors.name} onChange={(event) => update("name", event.target.value)} />
      <Input
        label="E-mail"
        name="email"
        type="email"
        value={form.email}
        autoComplete="email"
        placeholder="luis@email.com"
        error={errors.email}
        onChange={(event) => update("email", event.target.value)}
      />
      <Input
        label="Nome da conta financeira"
        name="accountName"
        value={form.accountName}
        placeholder="Financas do Luis"
        error={errors.accountName}
        onChange={(event) => update("accountName", event.target.value)}
      />
      <PasswordInput label="Senha" name="password" value={form.password} autoComplete="new-password" error={errors.password} onChange={(value) => update("password", value)} />
      <PasswordInput
        label="Confirmar senha"
        name="confirmPassword"
        value={form.confirmPassword}
        autoComplete="new-password"
        error={errors.confirmPassword}
        onChange={(value) => update("confirmPassword", value)}
      />

      <Button type="submit" className="h-11 w-full" disabled={loading}>
        {loading ? <Loader2 className="h-4 w-4 animate-spin" /> : <UserPlus className="h-4 w-4" />}
        Criar conta
      </Button>
      <button type="button" className="w-full text-sm font-bold text-zinc-300 transition hover:text-white" onClick={() => onBackToLogin()}>
        Voltar para login
      </button>
    </form>
  );
}
