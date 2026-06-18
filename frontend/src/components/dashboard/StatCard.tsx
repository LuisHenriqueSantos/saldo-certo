import clsx from "clsx";
import type { ReactNode } from "react";

type StatCardProps = {
  title: string;
  value: string;
  detail?: string;
  icon?: ReactNode;
  tone?: "default" | "orange" | "positive" | "negative";
};

export function StatCard({ title, value, detail, icon, tone = "default" }: StatCardProps) {
  return (
    <div
      className={clsx(
        "rounded-2xl border bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)]",
        tone === "default" && "border-zinc-800",
        tone === "orange" && "border-orange-500/35 bg-orange-500/10",
        tone === "positive" && "border-emerald-500/30 bg-emerald-500/10",
        tone === "negative" && "border-red-500/30 bg-red-500/10"
      )}
    >
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-xs font-black uppercase tracking-[0.18em] text-zinc-500">{title}</p>
          <p
            className={clsx(
              "mt-3 text-2xl font-black tracking-tight",
              tone === "orange" ? "text-orange-300" : tone === "positive" ? "text-emerald-300" : tone === "negative" ? "text-red-300" : "text-zinc-50"
            )}
          >
            {value}
          </p>
          {detail ? <p className="mt-2 text-xs font-medium text-zinc-500">{detail}</p> : null}
        </div>
        {icon ? (
          <span className="flex h-11 w-11 shrink-0 items-center justify-center rounded-2xl bg-zinc-950 text-orange-400 ring-1 ring-zinc-800">
            {icon}
          </span>
        ) : null}
      </div>
    </div>
  );
}
