"use client";

import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { EmptyState } from "@/components/ui/EmptyState";
import { money } from "@/lib/format";
import type { Dashboard } from "@/lib/types";

type IncomeExpenseChartProps = {
  data: Dashboard["incomeVsExpense"];
};

export function IncomeExpenseChart({ data }: IncomeExpenseChartProps) {
  const hasValues = data.some((item) => item.value > 0);

  return (
    <div className="rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)] xl:col-span-2">
      <h2 className="text-base font-black text-zinc-50">Receitas x despesas</h2>
      <p className="mt-1 text-sm text-zinc-500">Comparativo direto do mes.</p>
      <div className="mt-5 h-80">
        {hasValues ? (
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={data}>
              <CartesianGrid stroke="#27272a" strokeDasharray="3 3" vertical={false} />
              <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{ fill: "#9ca3af", fontSize: 12 }} />
              <YAxis axisLine={false} tickLine={false} tick={{ fill: "#9ca3af", fontSize: 12 }} tickFormatter={(value) => money.format(Number(value)).replace("R$", "")} />
              <Tooltip
                formatter={(value) => money.format(Number(value))}
                cursor={{ fill: "rgba(249,115,22,0.08)" }}
                contentStyle={{ background: "#0b0b0b", border: "1px solid #27272a", borderRadius: 14, color: "#f9fafb" }}
              />
              <Bar dataKey="value" radius={[10, 10, 0, 0]} fill="#f97316" />
            </BarChart>
          </ResponsiveContainer>
        ) : (
          <EmptyState title="Sem dados para comparar" description="Cadastre salario, receitas ou despesas para montar o grafico." />
        )}
      </div>
    </div>
  );
}
