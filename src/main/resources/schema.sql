-- Create database if not exists
CREATE DATABASE IF NOT EXISTS care_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE care_service_db;

-- Note: With spring.jpa.hibernate.ddl-auto=update, Hibernate will create tables automatically
-- This file provides additional SQL commands if needed

-- Add indexes for better performance (MySQL compatible syntax)
-- Indexes will be created by Hibernate based on @Index annotations in entities
-- These are backup commands if manual creation is needed

-- Uncomment below if you want to manually create indexes after tables exist
-- CREATE INDEX idx_users_email ON users(email);
-- CREATE INDEX idx_users_status ON users(status);
-- CREATE INDEX idx_bookings_status ON bookings(status);
-- CREATE INDEX idx_bookings_scheduled_start ON bookings(scheduled_start_time);
-- CREATE INDEX idx_payments_status ON payments(status);
-- CREATE INDEX idx_caregivers_verification ON caregivers(verification_status);
-- CREATE INDEX idx_support_tickets_status ON support_tickets(status);

