export type CategoryType = "INCOME" | "EXPENSE";
export type IncomeType = "FREELANCE" | "BONUS" | "COMMISSION" | "SALE" | "OTHER";
export type ExpenseStatus = "PENDING" | "PAID" | "OVERDUE";
export type ExpenseType = "FIXED" | "VARIABLE" | "INSTALLMENT" | "RECURRING";

export type Category = {
  id: string;
  name: string;
  type: CategoryType;
  color: string;
  icon: string;
  createdAt: string;
  updatedAt: string;
};

export type Salary = {
  id: string;
  description: string;
  amount: number;
  receivedDate: string;
  month: number;
  year: number;
  createdAt: string;
  updatedAt: string;
};

export type Income = {
  id: string;
  description: string;
  amount: number;
  incomeDate: string;
  type: IncomeType;
  month: number;
  year: number;
  createdAt: string;
  updatedAt: string;
};

export type Expense = {
  id: string;
  categoryId: string;
  categoryName: string;
  categoryColor: string;
  description: string;
  amount: number;
  dueDate: string;
  paymentDate: string | null;
  status: ExpenseStatus;
  type: ExpenseType;
  installmentNumber: number | null;
  totalInstallments: number | null;
  month: number;
  year: number;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
};

export type Dashboard = {
  month: number;
  year: number;
  totalSalary: number;
  totalExtraIncome: number;
  totalIncome: number;
  totalExpenses: number;
  totalPaid: number;
  totalPending: number;
  remainingBalance: number;
  committedPercentage: number;
  expensesByCategory: Array<{
    categoryId: string;
    categoryName: string;
    color: string;
    total: number;
  }>;
  incomeVsExpense: Array<{
    name: string;
    value: number;
  }>;
  upcomingBills: DashboardBill[];
  pendingBills: DashboardBill[];
  paidBills: DashboardBill[];
};

export type DashboardBill = {
  id: string;
  categoryId: string;
  categoryName: string;
  description: string;
  amount: number;
  dueDate: string;
  paymentDate: string | null;
  status: ExpenseStatus;
};

export type MonthlySummary = {
  id: string;
  month: number;
  year: number;
  totalSalary: number;
  totalExtraIncome: number;
  totalIncome: number;
  totalExpenses: number;
  totalPaid: number;
  totalPending: number;
  finalBalance: number;
  committedPercentage: number;
  closedAt: string;
};

export type ApiError = {
  message?: string;
  details?: string[];
};
