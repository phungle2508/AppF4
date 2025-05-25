
import { create } from 'zustand';
import { login as apiLogin, getUserInfo } from '@repo/api-client';

interface AuthState {
    accessToken: string | null;
    refreshToken: string | null;
    profile: any | null;
    loading: boolean;
    error: string | null;

    login: (username: string, password: string) => Promise<void>;
    logout: () => void;
    fetchProfile: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set, get) => ({
    accessToken: null,
    refreshToken: null,
    profile: null,
    loading: false,
    error: null,

    login: async (username, password) => {
        set({ loading: true, error: null });
        try {
            const tokens = await apiLogin(username, password);
            set({
                accessToken: tokens.access_token,
                refreshToken: tokens.refresh_token,
                loading: false,
            });
            await get().fetchProfile();
        } catch (err: any) {
            set({ error: err?.response?.data?.error || 'Login failed', loading: false });
        }
    },

    fetchProfile: async () => {
        const token = get().accessToken;
        if (!token) return;

        try {
            const profile = await getUserInfo(token);
            set({ profile });
        } catch {
            set({ profile: null });
        }
    },

    logout: () => {
        set({
            accessToken: null,
            refreshToken: null,
            profile: null,
            error: null,
        });
    },
}));
