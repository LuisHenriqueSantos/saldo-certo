import clsx from "clsx";

type FeedbackProps = {
  message: string | null;
  type?: "success" | "error";
};

export function Feedback({ message, type = "success" }: FeedbackProps) {
  if (!message) {
    return null;
  }

  return (
    <div
      className={clsx(
        "rounded-2xl border px-4 py-3 text-sm font-semibold",
        type === "success"
          ? "border-emerald-400/20 bg-emerald-400/10 text-emerald-200"
          : "border-red-400/20 bg-red-400/10 text-red-200"
      )}
    >
      {message}
    </div>
  );
}
