import { Inbox } from "lucide-react";

type EmptyStateProps = {
  title?: string;
  description?: string;
};

export function EmptyState({
  title = "Nenhum registro encontrado",
  description = "Quando houver dados para este periodo, eles aparecem aqui."
}: EmptyStateProps) {
  return (
    <div className="flex min-h-36 flex-col items-center justify-center rounded-2xl border border-dashed border-zinc-700 bg-zinc-950/50 p-6 text-center">
      <span className="mb-3 flex h-11 w-11 items-center justify-center rounded-2xl bg-zinc-900 text-zinc-500">
        <Inbox className="h-6 w-6" />
      </span>
      <p className="text-sm font-bold text-zinc-100">{title}</p>
      <p className="mt-1 max-w-sm text-xs leading-5 text-zinc-500">{description}</p>
    </div>
  );
}
