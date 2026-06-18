import { Check, Pencil, Trash2 } from "lucide-react";
import { StatusBadge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { EmptyState } from "@/components/ui/EmptyState";
import { dateLabel, money } from "@/lib/format";
import type { DashboardBill } from "@/lib/types";

type UpcomingBillsProps = {
  title: string;
  bills: DashboardBill[];
  empty: string;
  onPay?: (id: string) => void;
  onEdit?: (id: string) => void;
  onDelete?: (id: string) => void;
};

export function UpcomingBills({ title, bills, empty, onPay, onEdit, onDelete }: UpcomingBillsProps) {
  return (
    <div className="rounded-2xl border border-zinc-800 bg-zinc-900/85 p-5 shadow-[0_18px_55px_rgba(0,0,0,0.22)]">
      <h2 className="text-base font-black text-zinc-50">{title}</h2>
      {bills.length ? (
        <div className="mt-4 space-y-3">
          {bills.slice(0, 8).map((bill) => (
            <div key={bill.id} className="rounded-2xl border border-zinc-800 bg-zinc-950/55 p-4">
              <div className="flex items-start justify-between gap-3">
                <div>
                  <p className="font-bold text-zinc-100">{bill.description}</p>
                  <p className="mt-1 text-xs text-zinc-500">
                    {bill.categoryName} | vence {dateLabel(bill.dueDate)}
                  </p>
                </div>
                <StatusBadge status={bill.status} />
              </div>
              <div className="mt-3 flex flex-wrap items-center justify-between gap-3">
                <p className="text-lg font-black text-orange-300">{money.format(bill.amount)}</p>
                <div className="flex gap-2">
                  {onPay && bill.status !== "PAID" ? (
                    <Button variant="secondary" className="px-3 py-2" onClick={() => onPay(bill.id)} title="Marcar como pago">
                      <Check className="h-4 w-4" />
                    </Button>
                  ) : null}
                  {onEdit ? (
                    <Button variant="secondary" className="px-3 py-2" onClick={() => onEdit(bill.id)} title="Editar">
                      <Pencil className="h-4 w-4" />
                    </Button>
                  ) : null}
                  {onDelete ? (
                    <Button variant="danger" className="px-3 py-2" onClick={() => onDelete(bill.id)} title="Excluir">
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  ) : null}
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="mt-4">
          <EmptyState title={empty} />
        </div>
      )}
    </div>
  );
}
