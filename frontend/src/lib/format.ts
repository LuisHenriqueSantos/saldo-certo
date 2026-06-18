export const money = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL"
});

export const percent = new Intl.NumberFormat("pt-BR", {
  maximumFractionDigits: 2,
  minimumFractionDigits: 2
});

export const monthNames = [
  "Janeiro",
  "Fevereiro",
  "Marco",
  "Abril",
  "Maio",
  "Junho",
  "Julho",
  "Agosto",
  "Setembro",
  "Outubro",
  "Novembro",
  "Dezembro"
];

export function dateLabel(value: string | null) {
  if (!value) {
    return "-";
  }
  return new Intl.DateTimeFormat("pt-BR", { timeZone: "UTC" }).format(new Date(value));
}

export function statusLabel(status: string) {
  const map: Record<string, string> = {
    PENDING: "Pendente",
    PAID: "Pago",
    OVERDUE: "Atrasado"
  };
  return map[status] ?? status;
}

export function incomeTypeLabel(type: string) {
  const map: Record<string, string> = {
    FREELANCE: "Freelance",
    BONUS: "Bonus",
    COMMISSION: "Comissao",
    SALE: "Venda",
    OTHER: "Outro"
  };
  return map[type] ?? type;
}

export function expenseTypeLabel(type: string) {
  const map: Record<string, string> = {
    FIXED: "Fixo",
    VARIABLE: "Variavel",
    INSTALLMENT: "Parcelado",
    RECURRING: "Recorrente"
  };
  return map[type] ?? type;
}
