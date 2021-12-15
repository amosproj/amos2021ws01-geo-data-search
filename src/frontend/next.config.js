/** @type {import('next').NextConfig} */

module.exports = {
  async rewrites() {
    return process.env.BACKEND_API_ROOT
      ? [
          {
            source: '/api/:path*',
            destination: process.env.BACKEND_API_ROOT + '/:path*',
          },
        ]
      : [];
  },
  webpack(config) {
    config.module.rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
    });

    return config;
  },
  env: { 
    ENVIRONMENT: process.env.ENVIRONMENT,
  },
  reactStrictMode: true,
  swcMinify: false,
};
