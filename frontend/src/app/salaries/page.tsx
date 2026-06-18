"use client";

import { FormEvent, useEffect, useState } from "react";
import { Edit2, Trash2 } from "lucide-react";
import { Header } from "@/components/layout/Header";
import { EmptyState } from "@/components/ui/EmptyState";
import { Feedback } from "@/components/ui/Feedback";
import { LoadingPanel } from "@/components/ui/LoadingSkeleton";
import { MonthYearFilter } from "@/components/ui/MonthYearFilter";
import { api, queryMonthYear } from "@/lib/api";
import { dateLabel, money } from "@/lib/format";
import type { Salary } from "@/lib/types";

const now = new Date();
const currentMonth = now.getMonth() + 1;
const currentYear = now.getFullYear();

type SalaryForm = {
  id?: string;
  description: string;
  amount: string;
  receivedDate: string;
  month: number;
  year: number;
};

const initialForm = (): SalaryForm => ({
  description: "Salario principal",
  amount: "",
  receivedDate: now.toISOString().slice(0, 10),
  month: currentMonth,
  year: currentYear
});

export default function SalariesPage() {
  const [month, setMonth] = useState(currentMonth);
  const [year, setYear] = useState(currentYear);
  const [items, setItems] = useState<Salary[]>([]);
  const [form, setForm] = useState<SalaryForm>(initialForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      setItems(await api.get<Salary[]>(`/salaries?${queryMonthYear(month, year)}`));
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [month, year]);

  useEffect(() => {
    setForm((current) => ({ ...current, month, year }));
  }, [month, year]);

  async function submit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setSuccess(null);
    setError(null);
    const payload = { ...form, amount: Number(form.amount) };
    try {
      if (form.id) {
        await api.put<Salary>(`/salaries/${form.id}`, payload);
        setSuccess("Salario atualizado com sucesso.");
      } else {
        await api.post<Salary>("/salaries", payload);
        setSuccess("Salario cadastrado com sucesso.");
      }
      setForm({ ...initialForm(), month, year });
      await load();
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setSaving(false);
    }
  }

  async function remove(id: string) {
    setError(null);
    setSuccess(null);
    try {
      await api.delete(`/salaries/${id}`);
      setSuccess("Salario removido.");
      await load();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  return (
    <div className="space-y-6">
      <Header
        title="Salario"
        subtitle="Cadastre o salario principal do mes."
        actions={<MonthYearFilter month={month} year={year} onMonthChange={setMonth} onYearChange={setYear} />}
      />

      <Feedback message={success} />
      <Feedback message={error} type="error" />

      <form onSubmit={submit} className="grid gap-4 rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)] md:grid-cols-5">
        <label className="space-y-1 md:col-span-2">
          <span className="label">Descricao</span>
          <input className="field" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} required />
        </label>
        <label className="space-y-1">
          <span className="label">Valor</span>
          <input className="field" type="number" min="0.01" step="0.01" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
        </label>
        <label className="space-y-1">
          <span className="label">Recebimento</span>
          <input className="field" type="date" value={form.receivedDate} onChange={(e) => setForm({ ...form, receivedDate: e.target.value })} required />
        </label>
        <div className="flex items-end gap-2">
          <button className="btn-primary w-full" disabled={saving}>
            {form.id ? "Atualizar" : "Cadastrar"}
          </button>
          {form.id ? (
            <button type="button" className="btn-secondary" onClick={() => setForm({ ...initialForm(), month, year })}>
              Cancelar
            </button>
          ) : null}
        </div>
      </form>

      {loading ? (
        <LoadingPanel />
      ) : items.length ? (
        <div className="overflow-hidden rounded-2xl border border-zinc-800 bg-zinc-900/85 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
          <table className="w-full min-w-[720px]">
            <thead className="bg-zinc-950/70">
              <tr>
                <th className="table-th">Descricao</th>
                <th className="table-th">Valor</th>
                <th className="table-th">Recebimento</th>
                <th className="table-th">Acoes</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-zinc-800">
              {items.map((item) => (
                <tr key={item.id}>
                  <td className="table-td font-bold text-zinc-100">{item.description}</td>
                  <td className="table-td font-black text-orange-300">{money.format(item.amount)}</td>
                  <td className="table-td">{dateLabel(item.receivedDate)}</td>
                  <td className="table-td">
                    <div className="flex gap-2">
                      <button className="btn-secondary px-3" onClick={() => setForm({ id: item.id, description: item.description, amount: String(item.amount), receivedDate: item.receivedDate, month: item.month, year: item.year })}>
                        <Edit2 className="h-4 w-4" />
                      </button>
                      <button className="btn-secondary px-3 text-red-600" onClick={() => remove(item.id)}>
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <EmptyState title="Nenhum salario cadastrado para o periodo" />
      )}
    </div>
  );
}
