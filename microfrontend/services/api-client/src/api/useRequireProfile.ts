import { useAuthStore } from '@repo/zustand';
// in shared package

import { useEffect } from 'react';

export function useRequireProfile(router: any) {
    const profile = useAuthStore((s) => s.profile);
    const loading = useAuthStore((s) => s.loading);

    useEffect(() => {
        if (!loading && !profile) {
            router.push('/login');
        }
    }, [loading, profile, router]);

    return { profile, loading };
}