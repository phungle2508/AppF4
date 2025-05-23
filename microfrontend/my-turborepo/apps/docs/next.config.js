/** @type {import('next').NextConfig} */
const nextConfig = {
    basePath: '/docs',
    async rewrites() {
        return {
            beforeFiles: [
                {
                    source: '/_next/static/:path*',
                    destination: '/_next/static/:path*',
                },
            ],
        }
    },
};

export default nextConfig;
