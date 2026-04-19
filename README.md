# Chatapp API - Local Development with Docker Compose

This project can run locally with one Docker Compose command, including:

- Backend (`app` module)
- Local Supabase Postgres (Docker)
- Local Supabase Storage API (Docker)
- MinIO (S3 backend for local Supabase Storage)
- Redis
- RabbitMQ (+ management UI)
- Mailpit (SMTP + inbox UI for email testing)

## Prerequisites

- Docker Desktop (or Docker Engine + Compose plugin)

## Quick start (single command)

From the repository root:

```bash
docker compose up --build
```

That starts the backend and all local dependencies.

## Optional: customize environment variables

If you want to override defaults:

```bash
cp .env.example .env
```

Then edit `.env` and run:

```bash
docker compose up --build
```

## Seeded demo chat data (dev profile)

When running with the `dev` profile (default in compose), demo data is seeded at DB level by `supabase-db-seed`:

- User 1: `alice@test.com` / `12345Test` (username: `Alice`)
- User 2: `bob@test.com` / `12345Test` (username: `Bob`)
- One direct chat between them
- Three initial messages

Seeding is idempotent and can be re-run safely.

At minimum, set:

- `SPRING_DATASOURCE_PASSWORD` (defaults to `postgres` for local stack)
- `SUPABASE_SERVICE_KEY` (already prefilled in `.env.example` for local stack)
- `JWT_SECRET_BASE64` (must decode to at least 32 bytes for HS256)

## Service endpoints

- Backend API: `http://localhost:${BACKEND_PORT:-8080}`
- Supabase gateway (storage routes): `http://localhost:54321`
- Supabase Storage version check: `http://localhost:54321/storage/v1/version`
- Supabase DB (Postgres): `localhost:54322`
- MinIO API: `http://localhost:9000`
- MinIO Console: `http://localhost:9001`
- Mailpit UI: `http://localhost:8025`
- RabbitMQ UI: `http://localhost:15672` (default `guest` / `guest`)
- Redis: `localhost:6379`

## Notes about Supabase

- Compose runs Supabase-compatible services locally for both DB and storage.
- Backend uses `SUPABASE_URL` for internal calls and `SUPABASE_PUBLIC_URL` for returned public links.
- A lightweight local gateway exposes Supabase-compatible `/storage/v1/*` routes on port `54321`.
- Default storage bucket is `profile-pictures` and is created automatically at startup.
- This compose file is **not** the full Supabase stack (no local Supabase Studio/Admin UI).
- Upload and public URLs always use `SUPABASE_PUBLIC_URL` exactly as configured.

## `SUPABASE_PUBLIC_URL` (important)

For local compose, keep `SUPABASE_PUBLIC_URL=http://localhost:54321`.
The backend returns this value as-is in signed upload/public URLs.
Any platform-specific host mapping (emulator/device translation) must be handled on the client side.

## Supabase admin panel (Studio)

There is no Studio container in this compose setup, so there is nothing to "log in" to at `localhost:54321`.

For local inspection you can use:

- MinIO Console: `http://localhost:9001` (`minio` / `minio123456`) for object storage files.
- PostgreSQL clients on `localhost:54322` for database tables.

If you need the real Supabase Studio UI locally, run the Supabase CLI stack separately (`supabase start`) and point your client to that stack instead.

## Troubleshooting: port 8080 already in use

If Compose fails with `bind: address already in use` for `0.0.0.0:8080`, either stop the process using 8080 or change `BACKEND_PORT` in `.env`:

```bash
BACKEND_PORT=8081
docker compose up --build
```

## Troubleshooting: orphan container warning

If you see `Found orphan containers ([chatapp-postgres])`, clean old containers from previous compose versions:

```bash
docker compose down --remove-orphans
docker compose up --build
```

## Troubleshooting: `localhost:54322` does not open in browser

`54322` is a PostgreSQL TCP port, not an HTTP page, so browsers will show an error.
Use a DB client or run a quick check from Docker:

```bash
docker exec -it chatapp-supabase-db psql -U supabase_admin -d postgres -c "select now();"
```

If backend logs show `permission denied for schema ...`, ensure local DB user is `supabase_admin`
(`SPRING_DATASOURCE_USERNAME=supabase_admin`) and restart:

```bash
docker compose down -v
docker compose up --build
```

## Troubleshooting: image upload fails locally

Use this checklist:

1. Verify storage gateway:

```bash
curl -i http://localhost:54321/storage/v1/version
```

2. Verify browser preflight (CORS) to upload route:

```bash
curl -i -X OPTIONS "http://localhost:54321/storage/v1/object/upload/sign/profile-pictures/test.jpg" \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: authorization,content-type,apikey,x-client-info"
```

3. Ensure your app uses backend upload flow:
   - Call `POST /api/participants/profile-picture-upload?mimeType=image/jpeg`
   - Upload the file to returned `uploadUrl` with returned headers
   - Confirm with `POST /api/participants/confirm-profile-picture`

4. Keep `SUPABASE_PUBLIC_URL=http://localhost:54321` and ensure your client applies any required platform-side host translation.

5. If needed, recreate local volumes:

```bash
docker compose down -v
docker compose up --build
```

## Troubleshooting: `WeakKeyException` on startup

If backend fails with `WeakKeyException`, your `JWT_SECRET_BASE64` is too short.
Use a base64 value that decodes to at least 32 bytes (256 bits), for example:

```bash
JWT_SECRET_BASE64=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=
docker compose up --build
```

## Firebase credentials

Push notification startup expects Firebase Admin credentials at:

- `app/src/main/resources/firebase-credentials/chatapp-firebase-adminsdk.json`

If this file is missing, backend startup can fail.

## Stop everything

```bash
docker compose down
```

To remove volumes too:

```bash
docker compose down -v
```


