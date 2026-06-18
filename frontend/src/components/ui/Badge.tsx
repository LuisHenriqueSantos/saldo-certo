import clsx from "clsx";
import type { ExpenseStatus } from "@/lib/types";

export function StatusBadge({ status }: { status: ExpenseStatus }) {
  const styles = {
    PENDING: "border-amber-400/25 bg-amber-400/10 text-amber-200",
    PAID: "border-emerald-400/25 bg-emerald-400/10 text-emerald-200",
    OVERDUE: "border-red-400/25 bg-red-400/10 text-red-200"
  };

  const label = {
    PENDING: "Pendente",
    PAID: "Pago",
    OVERDUE: "Atrasado"
  };

  return (
    <span className={clsx("inline-flex rounded-full border px-2.5 py-1 text-xs font-bold", styles[status])}>
      {label[status]}
    </span>
  );
}
