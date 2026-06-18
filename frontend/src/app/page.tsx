"use client";

import { useEffect, useState } from "react";
import { BarChart3, CheckCircle2, CreditCard, LayoutDashboard, ShieldCheck, Wallet } from "lucide-react";
import { LoginForm } from "@/components/auth/LoginForm";
import { RegisterForm } from "@/components/auth/RegisterForm";
import { consumeSessionMessage, hasSession } from "@/lib/api";
import { useRouter } from "next/navigation";

const benefits = [
  "Controle de salário mensal",
  "Lançamento de receitas e despesas",
  "Dashboard com gráficos",
  "Contas pagas e pendentes",
  "Saldo restante do mês",
  "Resumo mensal"
];

export default function HomePage() {
  const router = useRouter();
  const [mode, setMode] = useState<"login" | "register">("login");
  const [success, setSuccess] = useState<string | null>(null);

  useEffect(() => {
    if (hasSession()) {
      router.replace("/dashboard");
      return;
    }
    setSuccess(consumeSessionMessage());
  }, [router]);

  return (
    <main className="min-h-screen bg-[#0f0f0f] text-zinc-50">
      <div className="pointer-events-none fixed inset-0 bg-[radial-gradient(circle_at_top_right,rgba(249,115,22,0.18),transparent_34%),radial-gradient(circle_at_18%_15%,rgba(255,255,255,0.06),transparent_28%)]" />
      <section className="relative mx-auto grid min-h-screen max-w-7xl items-center gap-8 px-4 py-8 sm:px-6 lg:grid-cols-[1.08fr_0.92fr] lg:px-8">
        <div className="space-y-8">
          <div className="inline-flex items-center gap-3 rounded-full border border-orange-500/25 bg-orange-500/10 px-4 py-2 text-sm font-bold text-orange-200">
            <ShieldCheck className="h-4 w-4" />
            Meu Saldo Mensal
          </div>

          <div className="max-w-3xl">
            <h1 className="text-4xl font-black tracking-tight text-zinc-50 sm:text-5xl lg:text-6xl">
              Controle seu mês financeiro com clareza.
            </h1>
            <p className="mt-5 max-w-2xl text-base leading-7 text-zinc-300 sm:text-lg">
              Cadastre seu salário, organize suas contas e veja automaticamente quanto ainda resta depois dos pagamentos.
            </p>
          </div>

          <div className="grid gap-3 sm:grid-cols-2">
            {benefits.map((benefit) => (
              <div key={benefit} className="flex items-center gap-3 rounded-2xl border border-zinc-800 bg-zinc-950/70 px-4 py-3 text-sm font-semibold text-zinc-200">
                <CheckCircle2 className="h-4 w-4 shrink-0 text-orange-300" />
                {benefit}
              </div>
            ))}
          </div>

          <div className="grid gap-3 sm:grid-cols-3">
            <div className="rounded-2xl border border-zinc-800 bg-zinc-950/70 p-4">
              <Wallet className="h-5 w-5 text-orange-300" />
              <p className="mt-3 text-sm font-bold">Salario e receitas</p>
              <p className="mt-1 text-xs leading-5 text-zinc-500">Entradas do mes sempre visiveis.</p>
            </div>
            <div className="rounded-2xl border border-zinc-800 bg-zinc-950/70 p-4">
              <CreditCard className="h-5 w-5 text-orange-300" />
              <p className="mt-3 text-sm font-bold">Despesas organizadas</p>
              <p className="mt-1 text-xs leading-5 text-zinc-500">Contas por status e vencimento.</p>
            </div>
            <div className="rounded-2xl border border-zinc-800 bg-zinc-950/70 p-4">
              <BarChart3 className="h-5 w-5 text-orange-300" />
              <p className="mt-3 text-sm font-bold">Resumo claro</p>
              <p className="mt-1 text-xs leading-5 text-zinc-500">Graficos e saldo restante.</p>
            </div>
          </div>
        </div>

        <div className="mx-auto w-full max-w-md rounded-3xl border border-zinc-800 bg-zinc-950/90 p-5 shadow-[0_24px_80px_rgba(0,0,0,0.36)] backdrop-blur">
          <div className="mb-6 flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-orange-500 text-white shadow-[0_0_30px_rgba(249,115,22,0.35)]">
              <LayoutDashboard className="h-6 w-6" />
            </div>
            <div>
              <p className="text-lg font-black">Meu Saldo Mensal</p>
              <p className="text-sm text-zinc-500">{mode === "login" ? "Acesse sua conta" : "Crie sua conta financeira"}</p>
            </div>
          </div>

          {success ? (
            <div className="mb-4 rounded-xl border border-emerald-500/25 bg-emerald-500/10 px-4 py-3 text-sm font-medium text-emerald-100">
              {success}
            </div>
          ) : null}

          {mode === "login" ? (
            <LoginForm onCreateAccount={() => {
              setSuccess(null);
              setMode("register");
            }} />
          ) : (
            <RegisterForm onBackToLogin={(message) => {
              setSuccess(message ?? null);
              setMode("login");
            }} />
          )}
        </div>
      </section>
    </main>
  );
}
