import clsx from "clsx";
import type { ButtonHTMLAttributes, ReactNode } from "react";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: "primary" | "secondary" | "danger" | "ghost";
  children: ReactNode;
};

export function Button({ variant = "primary", className, children, ...props }: ButtonProps) {
  return (
    <button
      className={clsx(
        "inline-flex items-center justify-center gap-2 rounded-xl px-4 py-2 text-sm font-semibold transition disabled:cursor-not-allowed disabled:opacity-55",
        variant === "primary" && "bg-orange-500 text-white shadow-[0_0_24px_rgba(249,115,22,0.22)] hover:bg-orange-600",
        variant === "secondary" && "border border-zinc-700 bg-zinc-900 text-zinc-100 hover:border-orange-500/70 hover:bg-zinc-800",
        variant === "danger" && "border border-red-500/30 bg-red-500/10 text-red-300 hover:bg-red-500/20",
        variant === "ghost" && "text-zinc-300 hover:bg-zinc-800 hover:text-white",
        className
      )}
      {...props}
    >
      {children}
    </button>
  );
}
