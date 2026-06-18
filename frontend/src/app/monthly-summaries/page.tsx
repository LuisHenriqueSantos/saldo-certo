"use client";

import { FormEvent, useEffect, useState } from "react";
import { Header } from "@/components/layout/Header";
import { EmptyState } from "@/components/ui/EmptyState";
import { Feedback } from "@/components/ui/Feedback";
import { LoadingPanel } from "@/components/ui/LoadingSkeleton";
import { api } from "@/lib/api";
import { dateLabel, money, monthNames, percent } from "@/lib/format";
import type { MonthlySummary } from "@/lib/types";

const now = new Date();
const currentMonth = now.getMonth() + 1;
const currentYear = now.getFullYear();

export default function MonthlySummariesPage() {
  const [year, setYear] = useState(currentYear);
  const [closeMonth, setCloseMonth] = useState(currentMonth);
  const [closeYear, setCloseYear] = useState(currentYear);
  const [items, setItems] = useState<MonthlySummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const years = Array.from({ length: 7 }, (_, index) => currentYear - 3 + index);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      setItems(await api.get<MonthlySummary[]>(`/monthly-summaries?year=${year}`));
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [year]);

  async function close(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setSuccess(null);
    setError(null);
    try {
      await api.post<MonthlySummary>("/monthly-summaries/close", { month: closeMonth, year: closeYear });
      setSuccess("Mes fechado com sucesso.");
      setYear(closeYear);
      await load();
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="space-y-6">
      <Header title="Resumo mensal" subtitle="Feche o mes e guarde o retrato financeiro calculado pelo dashboard." />

      <Feedback message={success} />
      <Feedback message={error} type="error" />

      <section className="grid gap-4 lg:grid-cols-[1fr_2fr]">
        <form onSubmit={close} className="space-y-4 rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
          <h2 className="text-sm font-black text-zinc-50">Fechar mes</h2>
          <label className="space-y-1 block">
            <span className="label">Mes</span>
            <select className="field" value={closeMonth} onChange={(event) => setCloseMonth(Number(event.target.value))}>
              {monthNames.map((name, index) => (
                <option key={name} value={index + 1}>{name}</option>
              ))}
            </select>
          </label>
          <label className="space-y-1 block">
            <span className="label">Ano</span>
            <select className="field" value={closeYear} onChange={(event) => setCloseYear(Number(event.target.value))}>
              {years.map((option) => (
                <option key={option} value={option}>{option}</option>
              ))}
            </select>
          </label>
          <button className="btn-primary w-full" disabled={saving}>Fechar mes</button>
        </form>

        <div className="rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h2 className="text-sm font-black text-zinc-50">Historico</h2>
              <p className="text-sm text-zinc-500">Resumos fechados por ano.</p>
            </div>
            <label className="space-y-1">
              <span className="label">Ano</span>
              <select className="field min-w-28" value={year} onChange={(event) => setYear(Number(event.target.value))}>
                {years.map((option) => (
                  <option key={option} value={option}>{option}</option>
                ))}
              </select>
            </label>
          </div>
        </div>
      </section>

      {loading ? (
        <LoadingPanel />
      ) : items.length ? (
        <div className="overflow-x-auto rounded-2xl border border-zinc-800 bg-zinc-900/85 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
          <table className="w-full min-w-[980px]">
            <thead className="bg-zinc-950/70">
              <tr>
                <th className="table-th">Periodo</th>
                <th className="table-th">Receitas</th>
                <th className="table-th">Despesas</th>
                <th className="table-th">Pago</th>
                <th className="table-th">Pendente</th>
                <th className="table-th">Saldo final</th>
                <th className="table-th">Comprometido</th>
                <th className="table-th">Fechado em</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-zinc-800">
              {items.map((item) => (
                <tr key={item.id}>
                  <td className="table-td font-bold text-zinc-100">{monthNames[item.month - 1]}/{item.year}</td>
                  <td className="table-td">{money.format(item.totalIncome)}</td>
                  <td className="table-td">{money.format(item.totalExpenses)}</td>
                  <td className="table-td">{money.format(item.totalPaid)}</td>
                  <td className="table-td">{money.format(item.totalPending)}</td>
                  <td className="table-td font-black text-orange-300">{money.format(item.finalBalance)}</td>
                  <td className="table-td">{percent.format(item.committedPercentage)}%</td>
                  <td className="table-td">{dateLabel(item.closedAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <EmptyState title="Nenhum resumo fechado para o ano" />
      )}
    </div>
  );
}
