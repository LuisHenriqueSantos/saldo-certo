"use client";

import type { ReactNode } from "react";
import { useEffect, useState } from "react";
import { usePathname, useRouter } from "next/navigation";
import { MobileNav, Sidebar } from "@/components/layout/Sidebar";
import { hasSession } from "@/lib/api";

export function AppLayout({ children }: { children: ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const [checked, setChecked] = useState(pathname === "/");
  const isPublic = pathname === "/";

  useEffect(() => {
    if (isPublic) {
      setChecked(true);
      return;
    }
    if (!hasSession()) {
      router.replace("/");
      return;
    }
    setChecked(true);
  }, [isPublic, router]);

  if (isPublic) {
    return <>{children}</>;
  }

  if (!checked) {
    return <div className="min-h-screen bg-[#0f0f0f]" />;
  }

  return (
    <div className="min-h-screen bg-[#0f0f0f] text-zinc-50">
      <div className="pointer-events-none fixed inset-0 bg-[radial-gradient(circle_at_top_right,rgba(249,115,22,0.16),transparent_34%),radial-gradient(circle_at_20%_10%,rgba(255,255,255,0.06),transparent_28%)]" />
      <Sidebar />
      <div className="relative lg:pl-80">
        <MobileNav />
        <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 lg:py-8">{children}</main>
      </div>
    </div>
  );
}
