// app/dashboard/page.tsx (or pages/dashboard.tsx, depending on your Next.js version)

'use client'
import { useRequireProfile } from "@repo/api-client";
import { useRouter } from "next/navigation"; // Change this line

// if using Next.js App Router


export default function DashboardPage() {
  const router = useRouter();
  const { profile, loading } = useRequireProfile(router);
  if (loading || !profile) {
    return <p>Loading...</p> // show spinner or skeleton while redirecting
  }

  return (
    <div>
      <h1>Welcome, {profile.name}!</h1>
      <p>This is your protected dashboard.</p>
    </div>
  )
}
