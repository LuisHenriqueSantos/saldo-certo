"use client";

import { Eye, EyeOff } from "lucide-react";
import { useState } from "react";

type PasswordInputProps = {
  label: string;
  value: string;
  onChange: (value: string) => void;
  name?: string;
  autoComplete?: string;
  error?: string | null;
};

export function PasswordInput({ label, value, onChange, name, autoComplete, error }: PasswordInputProps) {
  const [visible, setVisible] = useState(false);

  return (
    <label className="space-y-1">
      <span className="label">{label}</span>
      <span className="relative block">
        <input
          className="field pr-11"
          name={name}
          type={visible ? "text" : "password"}
          value={value}
          autoComplete={autoComplete}
          onChange={(event) => onChange(event.target.value)}
        />
        <button
          type="button"
          aria-label={visible ? "Ocultar senha" : "Mostrar senha"}
          className="absolute inset-y-0 right-2 my-auto flex h-8 w-8 items-center justify-center rounded-lg text-zinc-500 transition hover:bg-zinc-800 hover:text-orange-300"
          onClick={() => setVisible((current) => !current)}
        >
          {visible ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
        </button>
      </span>
      {error ? <span className="text-xs font-medium text-red-300">{error}</span> : null}
    </label>
  );
}
