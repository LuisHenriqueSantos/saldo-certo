import clsx from "clsx";
import type { InputHTMLAttributes } from "react";

type InputProps = InputHTMLAttributes<HTMLInputElement> & {
  label: string;
  error?: string | null;
};

export function Input({ label, error, className, id, ...props }: InputProps) {
  const inputId = id ?? props.name;

  return (
    <label className="space-y-1">
      <span className="label">{label}</span>
      <input id={inputId} className={clsx("field", className)} {...props} />
      {error ? <span className="text-xs font-medium text-red-300">{error}</span> : null}
    </label>
  );
}
