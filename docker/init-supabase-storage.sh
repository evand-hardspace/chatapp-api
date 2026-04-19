#!/bin/sh
set -e

GATEWAY_URL="${GATEWAY_URL:-http://supabase-gateway:8000}"
SERVICE_KEY="${SERVICE_KEY}"
BUCKET="${SUPABASE_STORAGE_BUCKET:-profile-pictures}"

echo "Waiting for Supabase Storage gateway..."
until curl -sf "${GATEWAY_URL}/storage/v1/version" > /dev/null; do
  sleep 2
done
echo "Gateway ready."

echo "Creating bucket '${BUCKET}'..."
curl -sf -X POST "${GATEWAY_URL}/storage/v1/bucket" \
  -H "Authorization: Bearer ${SERVICE_KEY}" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"${BUCKET}\",\"name\":\"${BUCKET}\",\"public\":true}" \
  && echo "Bucket '${BUCKET}' created." \
  || echo "Bucket '${BUCKET}' already exists or creation skipped."

