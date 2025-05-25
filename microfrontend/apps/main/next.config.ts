import { NextConfig } from 'next';

const { DOCS_URL, POST_URL } = process.env;

const nextConfig: NextConfig = {
  typescript: {
    ignoreBuildErrors: true,
  },
  eslint: {
    ignoreDuringBuilds: true,
  },
  reactStrictMode: true,
  transpilePackages: ["@repo/ui", "@repo/zustand"],
  output: "standalone",
  experimental: {
    esmExternals: 'loose'
  },

  async rewrites() {
    console.log('DOCS_URL', DOCS_URL);

    return [
      // Rewrites for Multi-Zones
      {
        source: '/docs',
        destination: `${DOCS_URL}`,
      },
      {
        source: '/docs/:path*',
        destination: `${DOCS_URL}/docs/:path*`,
      },
      {
        source: '/_next/static/:path*',
        destination: `${DOCS_URL}/_next/static/:path*`,
      },
      {
        source: '/docs-static/:path*',
        destination: `${DOCS_URL}/docs-static/:path*`,
      },
      // Rewrites for ShareSphere
      {
        source: '/post',
        destination: `${POST_URL}/post`,
      },
      {
        source: '/post/:path*',
        destination: `${POST_URL}/post/:path*`,
      },
      {
        source: '/sharesphere-static/:path*',
        destination: `${POST_URL}/sharesphere-static/:path*`,
      },
    ];
  },
};

export default nextConfig;
