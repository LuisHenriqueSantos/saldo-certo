import { AlertTriangle, RefreshCcw } from "lucide-react";
import { Button } from "@/components/ui/Button";

type AlertErrorProps = {
  message: string | null;
  onRetry?: () => void;
};

export function AlertError({ message, onRetry }: AlertErrorProps) {
  if (!message) {
    return null;
  }

  return (
    <div className="rounded-2xl border border-orange-500/25 bg-orange-500/10 p-4 text-orange-100 shadow-[0_0_35px_rgba(249,115,22,0.08)]">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex gap-3">
          <span className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-orange-500/15 text-orange-300">
            <AlertTriangle className="h-5 w-5" />
          </span>
          <div>
            <p className="font-semibold">Nao foi possivel conectar com a API.</p>
            <p className="mt-1 text-sm text-orange-100/75">{message}</p>
          </div>
        </div>
        {onRetry ? (
          <Button variant="secondary" onClick={onRetry}>
            <RefreshCcw className="h-4 w-4" />
            Tentar novamente
          </Button>
        ) : null}
      </div>
    </div>
  );
}
