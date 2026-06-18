"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import clsx from "clsx";
import {
  BarChart3,
  CircleDollarSign,
  CreditCard,
  LayoutDashboard,
  Tags,
  TrendingUp,
  Wallet
} from "lucide-react";

const navItems = [
  { href: "/", label: "Dashboard", icon: LayoutDashboard },
  { href: "/salaries", label: "Salario", icon: CircleDollarSign },
  { href: "/incomes", label: "Receitas", icon: Wallet },
  { href: "/expenses", label: "Despesas", icon: CreditCard },
  { href: "/categories", label: "Categorias", icon: Tags },
  { href: "/monthly-summaries", label: "Resumo", icon: BarChart3 }
];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="fixed inset-y-0 left-0 z-30 hidden w-80 border-r border-zinc-800 bg-[#0b0b0b] lg:block">
      <div className="flex h-full flex-col p-5">
        <div className="rounded-3xl border border-zinc-800 bg-zinc-950 p-4 shadow-[0_0_45px_rgba(249,115,22,0.08)]">
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-orange-500 text-white shadow-[0_0_30px_rgba(249,115,22,0.35)]">
              <TrendingUp className="h-6 w-6" />
            </div>
            <div>
              <p className="text-base font-black text-zinc-50">Saldo Certo</p>
              <p className="text-xs font-medium text-zinc-500">Controle financeiro pessoal</p>
            </div>
          </div>
        </div>

        <nav className="mt-7 flex-1 space-y-2">
          {navItems.map((item) => {
            const active = item.href === "/" ? pathname === "/" : pathname.startsWith(item.href);
            const Icon = item.icon;
            return (
              <Link
                key={item.href}
                href={item.href}
                className={clsx(
                  "group flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-bold transition",
                  active
                    ? "bg-orange-500 text-white shadow-[0_0_28px_rgba(249,115,22,0.25)]"
                    : "text-zinc-400 hover:bg-zinc-900 hover:text-zinc-50"
                )}
              >
                <span
                  className={clsx(
                    "flex h-9 w-9 items-center justify-center rounded-xl transition",
                    active ? "bg-white/15" : "bg-zinc-900 text-zinc-500 group-hover:bg-zinc-800 group-hover:text-orange-300"
                  )}
                >
                  <Icon className="h-4 w-4" />
                </span>
                {item.label}
              </Link>
            );
          })}
        </nav>

        <div className="rounded-3xl border border-zinc-800 bg-zinc-950 p-4">
          <p className="text-sm font-bold text-zinc-100">Controle do mes</p>
          <p className="mt-1 text-xs leading-5 text-zinc-500">Receitas, despesas e saldo final em um painel direto.</p>
        </div>
      </div>
    </aside>
  );
}

export function MobileNav() {
  const pathname = usePathname();

  return (
    <header className="sticky top-0 z-20 border-b border-zinc-800 bg-[#0b0b0b]/95 px-4 py-4 backdrop-blur lg:hidden">
      <div className="flex items-center justify-between gap-3">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-orange-500 text-white">
            <TrendingUp className="h-5 w-5" />
          </div>
          <div>
            <p className="font-black text-zinc-50">Saldo Certo</p>
            <p className="text-xs text-zinc-500">Controle financeiro pessoal</p>
          </div>
        </div>
      </div>
      <div className="mt-4 flex gap-2 overflow-x-auto pb-1">
        {navItems.map((item) => {
          const active = item.href === "/" ? pathname === "/" : pathname.startsWith(item.href);
          return (
            <Link
              key={item.href}
              href={item.href}
              className={clsx(
                "shrink-0 rounded-xl px-3 py-2 text-xs font-bold",
                active ? "bg-orange-500 text-white" : "bg-zinc-900 text-zinc-400"
              )}
            >
              {item.label}
            </Link>
          );
        })}
      </div>
    </header>
  );
}
