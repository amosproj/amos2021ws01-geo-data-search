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
  reactStrictMode: true,
  swcMinify: false,
};
