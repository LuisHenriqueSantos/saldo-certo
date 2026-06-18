import type { ReactNode } from "react";

type HeaderProps = {
  title: string;
  subtitle?: string;
  actions?: ReactNode;
};

export function Header({ title, subtitle, actions }: HeaderProps) {
  return (
    <div className="flex flex-col gap-5 border-b border-zinc-800 pb-6 xl:flex-row xl:items-end xl:justify-between">
      <div>
        <p className="text-xs font-black uppercase tracking-[0.25em] text-orange-400">Meu Saldo Mensal</p>
        <h1 className="mt-2 text-3xl font-black tracking-tight text-zinc-50">{title}</h1>
        {subtitle ? <p className="mt-2 max-w-2xl text-sm leading-6 text-zinc-400">{subtitle}</p> : null}
      </div>
      {actions ? <div className="shrink-0">{actions}</div> : null}
    </div>
  );
}
