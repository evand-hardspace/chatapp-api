-- Create application schemas required by Hibernate.
-- This script runs once on first DB initialisation (empty volume).
-- Hibernate's create_namespaces=true covers subsequent restarts with existing volumes.
CREATE SCHEMA IF NOT EXISTS user_service;
CREATE SCHEMA IF NOT EXISTS chat_service;
CREATE SCHEMA IF NOT EXISTS notification_service;
