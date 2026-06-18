"use client";

import { FormEvent, useEffect, useState } from "react";
import { Edit2, Trash2 } from "lucide-react";
import { Header } from "@/components/layout/Header";
import { EmptyState } from "@/components/ui/EmptyState";
import { Feedback } from "@/components/ui/Feedback";
import { LoadingPanel } from "@/components/ui/LoadingSkeleton";
import { api } from "@/lib/api";
import type { Category, CategoryType } from "@/lib/types";

type CategoryForm = {
  id?: string;
  name: string;
  type: CategoryType;
  color: string;
  icon: string;
};

const initialForm = (): CategoryForm => ({
  name: "",
  type: "EXPENSE",
  color: "#16a34a",
  icon: "Circle"
});

export default function CategoriesPage() {
  const [items, setItems] = useState<Category[]>([]);
  const [form, setForm] = useState<CategoryForm>(initialForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      setItems(await api.get<Category[]>("/categories"));
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function submit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setSuccess(null);
    setError(null);
    try {
      if (form.id) {
        await api.put<Category>(`/categories/${form.id}`, form);
        setSuccess("Categoria atualizada com sucesso.");
      } else {
        await api.post<Category>("/categories", form);
        setSuccess("Categoria cadastrada com sucesso.");
      }
      setForm(initialForm());
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
      await api.delete(`/categories/${id}`);
      setSuccess("Categoria removida.");
      await load();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  return (
    <div className="space-y-6">
      <Header title="Categorias" subtitle="Organize receitas e despesas por categorias personalizadas." />

      <Feedback message={success} />
      <Feedback message={error} type="error" />

      <form onSubmit={submit} className="grid gap-4 rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)] md:grid-cols-6">
        <label className="space-y-1 md:col-span-2">
          <span className="label">Nome</span>
          <input className="field" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
        </label>
        <label className="space-y-1">
          <span className="label">Tipo</span>
          <select className="field" value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value as CategoryType })}>
            <option value="EXPENSE">Despesa</option>
            <option value="INCOME">Receita</option>
          </select>
        </label>
        <label className="space-y-1">
          <span className="label">Cor</span>
          <input className="field h-10" type="color" value={form.color} onChange={(e) => setForm({ ...form, color: e.target.value })} />
        </label>
        <label className="space-y-1">
          <span className="label">Icone</span>
          <input className="field" value={form.icon} onChange={(e) => setForm({ ...form, icon: e.target.value })} required />
        </label>
        <div className="flex items-end gap-2">
          <button className="btn-primary w-full" disabled={saving}>{form.id ? "Atualizar" : "Cadastrar"}</button>
          {form.id ? <button type="button" className="btn-secondary" onClick={() => setForm(initialForm())}>Cancelar</button> : null}
        </div>
      </form>

      {loading ? (
        <LoadingPanel />
      ) : items.length ? (
        <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-3">
          {items.map((item) => (
            <div key={item.id} className="rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
              <div className="flex items-start justify-between gap-4">
                <div className="flex items-center gap-3">
                  <span className="h-4 w-4 rounded-full" style={{ backgroundColor: item.color }} />
                  <div>
                    <p className="font-bold text-zinc-50">{item.name}</p>
                    <p className="text-xs text-zinc-500">{item.type === "EXPENSE" ? "Despesa" : "Receita"} | {item.icon}</p>
                  </div>
                </div>
                <div className="flex gap-2">
                  <button className="btn-secondary px-3" onClick={() => setForm({ id: item.id, name: item.name, type: item.type, color: item.color, icon: item.icon })}>
                    <Edit2 className="h-4 w-4" />
                  </button>
                  <button className="btn-secondary px-3 text-red-600" onClick={() => remove(item.id)}>
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <EmptyState title="Nenhuma categoria cadastrada" />
      )}
    </div>
  );
}
