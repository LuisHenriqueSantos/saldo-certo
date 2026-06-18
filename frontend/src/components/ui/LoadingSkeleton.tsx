import clsx from "clsx";

export function LoadingSkeleton({ className }: { className?: string }) {
  return (
    <div className={clsx("animate-pulse rounded-2xl border border-zinc-800 bg-zinc-900/70 p-5", className)}>
      <div className="h-3 w-24 rounded-full bg-zinc-800" />
      <div className="mt-5 h-8 w-36 rounded-full bg-zinc-800" />
      <div className="mt-4 h-3 w-full rounded-full bg-zinc-800" />
      <div className="mt-2 h-3 w-2/3 rounded-full bg-zinc-800" />
    </div>
  );
}

export function LoadingPanel({ label = "Carregando..." }: { label?: string }) {
  return (
    <div className="flex min-h-40 items-center justify-center rounded-2xl border border-zinc-800 bg-zinc-900/60 text-sm font-semibold text-zinc-400">
      {label}
    </div>
  );
}
