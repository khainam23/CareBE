-- Insert Roles
INSERT INTO roles (id, name, description) VALUES 
(1, 'ROLE_ADMIN', 'Administrator role with full access'),
(2, 'ROLE_SUPPORT', 'Support staff role for handling tickets'),
(3, 'ROLE_CUSTOMER', 'Customer role for booking services'),
(4, 'ROLE_CAREGIVER', 'Caregiver role for providing services')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert Services
INSERT INTO services (id, name, description, category, base_price, duration_minutes, is_active, created_at, updated_at) VALUES
(1, 'Elderly Care', 'Professional care for elderly individuals including daily activities assistance', 'ELDERLY_CARE', 150000, 240, true, NOW(), NOW()),
(2, 'Child Care', 'Qualified childcare services for children of all ages', 'CHILD_CARE', 120000, 240, true, NOW(), NOW()),
(3, 'Medical Care', 'Medical care services including medication management and health monitoring', 'MEDICAL_CARE', 200000, 180, true, NOW(), NOW()),
(4, 'Companion Services', 'Companionship and social interaction services', 'COMPANION', 100000, 180, true, NOW(), NOW()),
(5, 'Housekeeping', 'Light housekeeping and meal preparation services', 'HOUSEKEEPING', 80000, 180, true, NOW(), NOW()),
(6, 'Nursing Care', 'Professional nursing care services', 'NURSING', 250000, 240, true, NOW(), NOW()),
(7, 'Rehabilitation Support', 'Post-surgery or injury rehabilitation support', 'REHABILITATION', 180000, 180, true, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert Admin User (password: admin123)
-- BCrypt hash for 'admin123'
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(1, 'admin@careservice.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', '0901234567', 'Ho Chi Minh City', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Insert Support User (password: support123)
-- BCrypt hash for 'support123'
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(2, 'support@careservice.com', '$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW', 'Support Agent', '0902234567', 'Ho Chi Minh City', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 2)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Insert Sample Customer (password: customer123)
-- BCrypt hash for 'customer123'
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(3, 'customer@example.com', '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6', 'Nguyen Van A', '0903234567', '123 Nguyen Hue, District 1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (3, 3)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO customers (id, user_id, emergency_contact, emergency_phone, total_spent, booking_count, created_at, updated_at) VALUES
(1, 3, 'Nguyen Thi B', '0904234567', 0, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Insert Sample Caregiver (password: caregiver123)
-- BCrypt hash for 'caregiver123'
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(4, 'caregiver@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tran Thi C', '0904234567', '456 Le Loi, District 3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (4, 4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(1, 4, 'Experienced caregiver with 5 years of experience in elderly care', 'Elderly care, Medical care, Companion services', '5 years in professional caregiving', '079123456789', 'APPROVED', true, 50000, 4.8, 25, 30, 15000000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Insert Additional Caregiver Users
-- Caregiver 2 (password: caregiver123)
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(5, 'caregiver2@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Le Van D', '0905234567', '789 Nguyen Trai, District 5, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (5, 4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(2, 5, 'Chuyên chăm sóc trẻ em với 3 năm kinh nghiệm, có chứng chỉ sơ cứu', 'Child care, Housekeeping, Companion services', '3 years specializing in childcare and early childhood development', '079234567890', 'APPROVED', true, 45000, 4.6, 18, 22, 9800000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 3 (password: caregiver123)
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(6, 'caregiver3@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Pham Thi E', '0906234567', '321 Vo Van Tan, District 3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (6, 4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(3, 6, 'Y tá có kinh nghiệm 7 năm trong chăm sóc y tế tại nhà, chuyên về bệnh mãn tính', 'Medical care, Nursing care, Rehabilitation support', '7 years as registered nurse, specialized in home healthcare', '079345678901', 'APPROVED', true, 70000, 4.9, 42, 55, 38500000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 4 (password: caregiver123)
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(7, 'caregiver4@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hoang Van F', '0907234567', '654 Ly Tu Trong, District 1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (7, 4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(4, 7, 'Chăm sóc người cao tuổi và hỗ trợ phục hồi chức năng, có kinh nghiệm 4 năm', 'Elderly care, Rehabilitation support, Companion services', '4 years in elderly care and rehabilitation therapy', '079456789012', 'APPROVED', true, 55000, 4.7, 32, 40, 22000000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 5 (password: caregiver123)
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(8, 'caregiver5@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tran Thi G', '0908234567', '987 Pasteur, District 3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (8, 4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(5, 8, 'Chuyên viên chăm sóc đa năng với 6 năm kinh nghiệm, thành thạo nhiều loại dịch vụ', 'Elderly care, Child care, Housekeeping, Companion services', '6 years providing comprehensive care services', '079567890123', 'APPROVED', true, 60000, 4.8, 38, 48, 28800000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 6 (password: caregiver123) - Pending verification
INSERT INTO users (id, email, password, full_name, phone_number, address, status, enabled, created_at, updated_at) VALUES
(9, 'caregiver6@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Nguyen Van H', '0909234567', '147 Tran Hung Dao, District 1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles (user_id, role_id) VALUES (9, 4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(6, 9, 'Người chăm sóc mới, đang chờ xác minh hồ sơ', 'Elderly care, Companion services', '2 years experience, recently joined platform', '079678901234', 'PENDING', false, 40000, 0.0, 0, 0, 0, NOW(), NOW(), NULL)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);