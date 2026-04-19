-- Idempotent demo seed for local development.
-- Runs after backend creates Hibernate-managed tables.

BEGIN;

INSERT INTO user_service.users (id, email, username, hashed_password, has_verified_email, created_at, updated_at)
VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'alice@test.com', 'Alice', '$2b$10$ocUV2KsOmodmT1BV7A7n6.telQ5ftlnuCLi7hCPp5DGaPklW5WPbW', true, now(), now()),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'bob@test.com', 'Bob', '$2b$10$ocUV2KsOmodmT1BV7A7n6.telQ5ftlnuCLi7hCPp5DGaPklW5WPbW', true, now(), now())
ON CONFLICT (id) DO UPDATE
SET
  email = EXCLUDED.email,
  username = EXCLUDED.username,
  hashed_password = EXCLUDED.hashed_password,
  has_verified_email = EXCLUDED.has_verified_email,
  updated_at = now();

INSERT INTO chat_service.chat_participants (user_id, username, email, profile_picture_url, created_at)
VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Alice', 'alice@test.com', NULL, now()),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'Bob', 'bob@test.com', NULL, now())
ON CONFLICT (user_id) DO UPDATE
SET
  username = EXCLUDED.username,
  email = EXCLUDED.email,
  profile_picture_url = EXCLUDED.profile_picture_url;

INSERT INTO chat_service.chats (id, creator_id, created_at)
VALUES ('cccccccc-cccc-cccc-cccc-ccccccccccc3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO chat_service.chat_participants_cross_ref (chat_id, user_id)
VALUES
  ('cccccccc-cccc-cccc-cccc-ccccccccccc3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
  ('cccccccc-cccc-cccc-cccc-ccccccccccc3', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2')
ON CONFLICT DO NOTHING;

INSERT INTO chat_service.chat_messages (id, content, chat_id, sender_id, created_at)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Hey Bob, this is seeded local data.', 'cccccccc-cccc-cccc-cccc-ccccccccccc3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', now()),
  ('22222222-2222-2222-2222-222222222222', 'Nice, I can see the message history now.', 'cccccccc-cccc-cccc-cccc-ccccccccccc3', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', now()),
  ('33333333-3333-3333-3333-333333333333', 'Let us test profile pictures from Android next.', 'cccccccc-cccc-cccc-cccc-ccccccccccc3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', now())
ON CONFLICT (id) DO UPDATE
SET
  content = EXCLUDED.content,
  sender_id = EXCLUDED.sender_id;

COMMIT;

