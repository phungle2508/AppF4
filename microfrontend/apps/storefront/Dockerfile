FROM node:18-alpine AS base

# Enable Corepack just in case it's needed
RUN corepack enable

FROM base AS builder
RUN apk update && apk add --no-cache libc6-compat
WORKDIR /app

# Use npx instead of yarn global
COPY . .
RUN npx turbo prune storefront --docker

FROM base AS installer
RUN apk update && apk add --no-cache libc6-compat
WORKDIR /app

# First install the dependencies
COPY --from=builder /app/out/json/ .
RUN npm install

# Build the project
COPY --from=builder /app/out/full/ .
RUN npx turbo build --filter=storefront...

FROM base AS runner
WORKDIR /app

# Don't run production as root
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs
USER nextjs

# Copy the standalone output first
COPY --from=installer --chown=nextjs:nodejs /app/apps/storefront/.next/standalone ./
COPY --from=installer --chown=nextjs:nodejs /app/apps/storefront/.next/static ./apps/storefront/.next/static
COPY --from=installer --chown=nextjs:nodejs /app/apps/storefront/public ./apps/storefront/public

# Copy the source package.json to ensure it has "type": "module"
COPY --from=installer /app/apps/storefront/package.json ./package.json


CMD ["node", "apps/storefront/server.js"]
