"use client";

import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from "recharts";
import { EmptyState } from "@/components/ui/EmptyState";
import { money } from "@/lib/format";
import type { Dashboard } from "@/lib/types";

type ExpenseCategoryChartProps = {
  data: Dashboard["expensesByCategory"];
};

export function ExpenseCategoryChart({ data }: ExpenseCategoryChartProps) {
  return (
    <div className="rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)] xl:col-span-3">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h2 className="text-base font-black text-zinc-50">Gastos por categoria</h2>
          <p className="mt-1 text-sm text-zinc-500">Onde o dinheiro esta indo no periodo.</p>
        </div>
      </div>
      <div className="mt-5 h-80">
        {data.length ? (
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie data={data} dataKey="total" nameKey="categoryName" innerRadius={72} outerRadius={116} paddingAngle={3}>
                {data.map((entry, index) => (
                  <Cell key={entry.categoryId} fill={index === 0 ? "#f97316" : entry.color} stroke="#18181b" />
                ))}
              </Pie>
              <Tooltip
                formatter={(value) => money.format(Number(value))}
                contentStyle={{ background: "#0b0b0b", border: "1px solid #27272a", borderRadius: 14, color: "#f9fafb" }}
                itemStyle={{ color: "#f9fafb" }}
              />
            </PieChart>
          </ResponsiveContainer>
        ) : (
          <EmptyState title="Sem despesas para o grafico" description="Cadastre despesas para visualizar a distribuicao por categoria." />
        )}
      </div>
    </div>
  );
}
