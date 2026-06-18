"use client";

import { FormEvent, useEffect, useState } from "react";
import { Check, Edit2, RotateCcw, Trash2 } from "lucide-react";
import { Header } from "@/components/layout/Header";
import { StatusBadge } from "@/components/ui/Badge";
import { EmptyState } from "@/components/ui/EmptyState";
import { Feedback } from "@/components/ui/Feedback";
import { LoadingPanel } from "@/components/ui/LoadingSkeleton";
import { MonthYearFilter } from "@/components/ui/MonthYearFilter";
import { api, queryMonthYear } from "@/lib/api";
import { dateLabel, expenseTypeLabel, money } from "@/lib/format";
import type { Category, Expense, ExpenseStatus, ExpenseType } from "@/lib/types";

const now = new Date();
const currentMonth = now.getMonth() + 1;
const currentYear = now.getFullYear();
const expenseTypes: ExpenseType[] = ["FIXED", "VARIABLE", "INSTALLMENT", "RECURRING"];
const statusOptions: ExpenseStatus[] = ["PENDING", "PAID"];

type ExpenseForm = {
  id?: string;
  categoryId: string;
  description: string;
  amount: string;
  dueDate: string;
  paymentDate: string;
  status: ExpenseStatus;
  type: ExpenseType;
  installmentNumber: string;
  totalInstallments: string;
  month: number;
  year: number;
  notes: string;
};

const initialForm = (): ExpenseForm => ({
  categoryId: "",
  description: "",
  amount: "",
  dueDate: now.toISOString().slice(0, 10),
  paymentDate: "",
  status: "PENDING",
  type: "VARIABLE",
  installmentNumber: "",
  totalInstallments: "",
  month: currentMonth,
  year: currentYear,
  notes: ""
});

export default function ExpensesPage() {
  const [month, setMonth] = useState(currentMonth);
  const [year, setYear] = useState(currentYear);
  const [items, setItems] = useState<Expense[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [form, setForm] = useState<ExpenseForm>(initialForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const [expenseResult, categoryResult] = await Promise.all([
        api.get<Expense[]>(`/expenses?${queryMonthYear(month, year)}`),
        api.get<Category[]>("/categories")
      ]);
      setItems(expenseResult);
      const expenseCategories = categoryResult.filter((category) => category.type === "EXPENSE");
      setCategories(expenseCategories);
      setForm((current) => ({
        ...current,
        categoryId: current.categoryId || expenseCategories[0]?.id || ""
      }));
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
    const payload = {
      categoryId: form.categoryId,
      description: form.description,
      amount: Number(form.amount),
      dueDate: form.dueDate,
      paymentDate: form.paymentDate || null,
      status: form.status,
      type: form.type,
      installmentNumber: form.installmentNumber ? Number(form.installmentNumber) : null,
      totalInstallments: form.totalInstallments ? Number(form.totalInstallments) : null,
      month: form.month,
      year: form.year,
      notes: form.notes || null
    };

    try {
      if (form.id) {
        await api.put<Expense>(`/expenses/${form.id}`, payload);
        setSuccess("Despesa atualizada com sucesso.");
      } else {
        await api.post<Expense>("/expenses", payload);
        setSuccess("Despesa cadastrada com sucesso.");
      }
      setForm({ ...initialForm(), month, year, categoryId: categories[0]?.id || "" });
      await load();
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setSaving(false);
    }
  }

  async function remove(id: string) {
    setSuccess(null);
    setError(null);
    try {
      await api.delete(`/expenses/${id}`);
      setSuccess("Despesa removida.");
      await load();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  async function pay(id: string) {
    setSuccess(null);
    setError(null);
    try {
      await api.patch<Expense>(`/expenses/${id}/pay`);
      setSuccess("Despesa marcada como paga.");
      await load();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  async function pending(id: string) {
    setSuccess(null);
    setError(null);
    try {
      await api.patch<Expense>(`/expenses/${id}/pending`);
      setSuccess("Despesa voltou para pendente.");
      await load();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  function edit(item: Expense) {
    setForm({
      id: item.id,
      categoryId: item.categoryId,
      description: item.description,
      amount: String(item.amount),
      dueDate: item.dueDate,
      paymentDate: item.paymentDate ?? "",
      status: item.status === "OVERDUE" ? "PENDING" : item.status,
      type: item.type,
      installmentNumber: item.installmentNumber ? String(item.installmentNumber) : "",
      totalInstallments: item.totalInstallments ? String(item.totalInstallments) : "",
      month: item.month,
      year: item.year,
      notes: item.notes ?? ""
    });
  }

  return (
    <div className="space-y-6">
      <Header
        title="Despesas"
        subtitle="Lance contas fixas, variaveis, parceladas e recorrentes."
        actions={<MonthYearFilter month={month} year={year} onMonthChange={setMonth} onYearChange={setYear} />}
      />

      <Feedback message={success} />
      <Feedback message={error} type="error" />

      <form onSubmit={submit} className="grid gap-4 rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)] lg:grid-cols-6">
        <label className="space-y-1 lg:col-span-2">
          <span className="label">Descricao</span>
          <input className="field" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} required />
        </label>
        <label className="space-y-1">
          <span className="label">Valor</span>
          <input className="field" type="number" min="0.01" step="0.01" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
        </label>
        <label className="space-y-1">
          <span className="label">Categoria</span>
          <select className="field" value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} required>
            <option value="" disabled>Selecione</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>{category.name}</option>
            ))}
          </select>
        </label>
        <label className="space-y-1">
          <span className="label">Vencimento</span>
          <input className="field" type="date" value={form.dueDate} onChange={(e) => setForm({ ...form, dueDate: e.target.value })} required />
        </label>
        <label className="space-y-1">
          <span className="label">Pagamento</span>
          <input className="field" type="date" value={form.paymentDate} onChange={(e) => setForm({ ...form, paymentDate: e.target.value, status: e.target.value ? "PAID" : form.status })} />
        </label>
        <label className="space-y-1">
          <span className="label">Status</span>
          <select className="field" value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value as ExpenseStatus })}>
            {statusOptions.map((status) => (
              <option key={status} value={status}>{status === "PAID" ? "Pago" : "Pendente"}</option>
            ))}
          </select>
        </label>
        <label className="space-y-1">
          <span className="label">Tipo</span>
          <select className="field" value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value as ExpenseType })}>
            {expenseTypes.map((type) => (
              <option key={type} value={type}>{expenseTypeLabel(type)}</option>
            ))}
          </select>
        </label>
        <label className="space-y-1">
          <span className="label">Parcela</span>
          <input className="field" type="number" min="1" value={form.installmentNumber} onChange={(e) => setForm({ ...form, installmentNumber: e.target.value })} />
        </label>
        <label className="space-y-1">
          <span className="label">Total parcelas</span>
          <input className="field" type="number" min="1" value={form.totalInstallments} onChange={(e) => setForm({ ...form, totalInstallments: e.target.value })} />
        </label>
        <label className="space-y-1 lg:col-span-2">
          <span className="label">Observacao</span>
          <input className="field" value={form.notes} onChange={(e) => setForm({ ...form, notes: e.target.value })} />
        </label>
        <div className="flex items-end gap-2">
          <button className="btn-primary w-full" disabled={saving || !categories.length}>{form.id ? "Atualizar" : "Cadastrar"}</button>
          {form.id ? <button type="button" className="btn-secondary" onClick={() => setForm({ ...initialForm(), month, year, categoryId: categories[0]?.id || "" })}>Cancelar</button> : null}
        </div>
      </form>

      {loading ? (
        <LoadingPanel />
      ) : items.length ? (
        <div className="overflow-x-auto rounded-2xl border border-zinc-800 bg-zinc-900/85 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
          <table className="w-full min-w-[1080px]">
            <thead className="bg-zinc-950/70">
              <tr>
                <th className="table-th">Descricao</th>
                <th className="table-th">Categoria</th>
                <th className="table-th">Valor</th>
                <th className="table-th">Vencimento</th>
                <th className="table-th">Pagamento</th>
                <th className="table-th">Status</th>
                <th className="table-th">Tipo</th>
                <th className="table-th">Acoes</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-zinc-800">
              {items.map((item) => (
                <tr key={item.id}>
                  <td className="table-td font-bold text-zinc-100">{item.description}</td>
                  <td className="table-td">
                    <span className="inline-flex items-center gap-2">
                      <span className="h-3 w-3 rounded-full" style={{ backgroundColor: item.categoryColor }} />
                      {item.categoryName}
                    </span>
                  </td>
                  <td className="table-td font-black text-orange-300">{money.format(item.amount)}</td>
                  <td className="table-td">{dateLabel(item.dueDate)}</td>
                  <td className="table-td">{dateLabel(item.paymentDate)}</td>
                  <td className="table-td"><StatusBadge status={item.status} /></td>
                  <td className="table-td">{expenseTypeLabel(item.type)}</td>
                  <td className="table-td">
                    <div className="flex gap-2">
                      {item.status === "PAID" ? (
                        <button className="btn-secondary px-3" onClick={() => pending(item.id)} title="Voltar para pendente">
                          <RotateCcw className="h-4 w-4" />
                        </button>
                      ) : (
                        <button className="btn-secondary px-3 text-orange-300" onClick={() => pay(item.id)} title="Marcar como paga">
                          <Check className="h-4 w-4" />
                        </button>
                      )}
                      <button className="btn-secondary px-3" onClick={() => edit(item)} title="Editar">
                        <Edit2 className="h-4 w-4" />
                      </button>
                      <button className="btn-secondary px-3 text-red-600" onClick={() => remove(item.id)} title="Excluir">
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
        <EmptyState title="Nenhuma despesa cadastrada para o periodo" />
      )}
    </div>
  );
}
