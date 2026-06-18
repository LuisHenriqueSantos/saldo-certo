"use client";

import { monthNames } from "@/lib/format";

type MonthYearFilterProps = {
  month: number;
  year: number;
  onMonthChange: (month: number) => void;
  onYearChange: (year: number) => void;
};

export function MonthYearFilter({ month, year, onMonthChange, onYearChange }: MonthYearFilterProps) {
  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 7 }, (_, index) => currentYear - 3 + index);

  return (
    <div className="flex flex-col gap-3 sm:flex-row">
      <label className="space-y-1">
        <span className="label">Mes</span>
        <select className="field min-w-40" value={month} onChange={(event) => onMonthChange(Number(event.target.value))}>
          {monthNames.map((name, index) => (
            <option key={name} value={index + 1}>
              {name}
            </option>
          ))}
        </select>
      </label>
      <label className="space-y-1">
        <span className="label">Ano</span>
        <select className="field min-w-28" value={year} onChange={(event) => onYearChange(Number(event.target.value))}>
          {years.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
      </label>
    </div>
  );
}
