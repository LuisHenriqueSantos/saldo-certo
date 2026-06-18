"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { AlertTriangle, CheckCircle2, Clock, CreditCard, Percent, TrendingDown, TrendingUp, Wallet } from "lucide-react";
import { ExpenseCategoryChart } from "@/components/dashboard/ExpenseCategoryChart";
import { IncomeExpenseChart } from "@/components/dashboard/IncomeExpenseChart";
import { StatCard } from "@/components/dashboard/StatCard";
import { UpcomingBills } from "@/components/dashboard/UpcomingBills";
import { Header } from "@/components/layout/Header";
import { AlertError } from "@/components/ui/AlertError";
import { LoadingSkeleton } from "@/components/ui/LoadingSkeleton";
import { MonthYearFilter } from "@/components/ui/MonthYearFilter";
import { api, queryMonthYear } from "@/lib/api";
import { money, percent } from "@/lib/format";
import type { Dashboard } from "@/lib/types";

const today = new Date();

function emptyDashboard(month: number, year: number): Dashboard {
  return {
    month,
    year,
    totalSalary: 0,
    totalExtraIncome: 0,
    totalIncome: 0,
    totalExpenses: 0,
    totalPaid: 0,
    totalPending: 0,
    remainingBalance: 0,
    committedPercentage: 0,
    expensesByCategory: [],
    incomeVsExpense: [
      { name: "Receitas", value: 0 },
      { name: "Despesas", value: 0 }
    ],
    upcomingBills: [],
    pendingBills: [],
    paidBills: []
  };
}

export default function DashboardPage() {
  const [month, setMonth] = useState(today.getMonth() + 1);
  const [year, setYear] = useState(today.getFullYear());
  const [dashboard, setDashboard] = useState<Dashboard | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadDashboard = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await api.get<Dashboard>(`/dashboard?${queryMonthYear(month, year)}`);
      setDashboard(result);
    } catch (err) {
      setDashboard(emptyDashboard(month, year));
      setError((err as Error).message);
    } finally {
      setLoading(false);
    }
  }, [month, year]);

  useEffect(() => {
    loadDashboard();
  }, [loadDashboard]);

  const view = useMemo(() => dashboard ?? emptyDashboard(month, year), [dashboard, month, year]);
  const isPositive = view.remainingBalance >= 0;

  async function markAsPaid(id: string) {
    try {
      await api.patch(`/expenses/${id}/pay`);
      await loadDashboard();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  return (
    <div className="space-y-6">
      <Header
        title="Dashboard mensal"
        subtitle="Acompanhe receitas, contas, saldo restante e percentual comprometido em um painel direto."
        actions={<MonthYearFilter month={month} year={year} onMonthChange={setMonth} onYearChange={setYear} />}
      />

      <AlertError message={error} onRetry={loadDashboard} />

      {loading ? (
        <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {Array.from({ length: 8 }).map((_, index) => (
            <LoadingSkeleton key={index} />
          ))}
        </section>
      ) : (
        <>
          <div
            className={`rounded-2xl border p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)] ${
              isPositive ? "border-emerald-500/25 bg-emerald-500/10 text-emerald-100" : "border-red-500/25 bg-red-500/10 text-red-100"
            }`}
          >
            <div className="flex items-center gap-3">
              <span className="flex h-11 w-11 items-center justify-center rounded-2xl bg-black/20">
                {isPositive ? <CheckCircle2 className="h-5 w-5" /> : <AlertTriangle className="h-5 w-5" />}
              </span>
              <div>
                <p className="font-black">{isPositive ? "Mes positivo" : "Mes negativo"}</p>
                <p className="mt-1 text-sm opacity-80">
                  {isPositive
                    ? `Saldo previsto de ${money.format(view.remainingBalance)} depois das despesas.`
                    : `Os gastos superam as receitas em ${money.format(Math.abs(view.remainingBalance))}.`}
                </p>
              </div>
            </div>
          </div>

          <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
            <StatCard title="Salario do mes" value={money.format(view.totalSalary)} icon={<Wallet className="h-5 w-5" />} tone="orange" />
            <StatCard title="Receitas extras" value={money.format(view.totalExtraIncome)} icon={<TrendingUp className="h-5 w-5" />} />
            <StatCard title="Total recebido" value={money.format(view.totalIncome)} icon={<Wallet className="h-5 w-5" />} tone="orange" />
            <StatCard title="Total gasto" value={money.format(view.totalExpenses)} icon={<CreditCard className="h-5 w-5" />} />
            <StatCard title="Total pago" value={money.format(view.totalPaid)} icon={<CheckCircle2 className="h-5 w-5" />} />
            <StatCard title="Total pendente" value={money.format(view.totalPending)} icon={<Clock className="h-5 w-5" />} />
            <StatCard
              title="Saldo restante"
              value={money.format(view.remainingBalance)}
              icon={isPositive ? <TrendingUp className="h-5 w-5" /> : <TrendingDown className="h-5 w-5" />}
              tone={isPositive ? "positive" : "negative"}
            />
            <StatCard title="Comprometido" value={`${percent.format(view.committedPercentage)}%`} detail="despesas sobre receitas" icon={<Percent className="h-5 w-5" />} />
          </section>

          <section className="grid gap-4 xl:grid-cols-5">
            <ExpenseCategoryChart data={view.expensesByCategory} />
            <IncomeExpenseChart data={view.incomeVsExpense} />
          </section>

          <section className="grid gap-4 xl:grid-cols-3">
            <UpcomingBills title="Proximas contas" bills={view.upcomingBills} empty="Nenhuma conta vence nos proximos 7 dias" onPay={markAsPaid} />
            <UpcomingBills title="Contas pendentes" bills={view.pendingBills} empty="Nenhuma conta pendente" onPay={markAsPaid} />
            <UpcomingBills title="Contas pagas" bills={view.paidBills} empty="Nenhuma conta paga" />
          </section>
        </>
      )}
    </div>
  );
}
