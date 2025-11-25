-- Insert Roles
INSERT INTO roles (id, name, description) VALUES 
(1, 'ROLE_ADMIN', 'Administrator role with full access'),
(2, 'ROLE_SUPPORT', 'Support staff role for handling tickets'),
(3, 'ROLE_CUSTOMER', 'Customer role for booking services'),
(4, 'ROLE_CAREGIVER', 'Caregiver role for providing services')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert Services
INSERT INTO services (id, name, description, category, base_price, duration_minutes, is_active, created_at, updated_at) VALUES
(1, 'Chăm sóc người già', 'Dịch vụ chăm sóc chuyên nghiệp cho người cao tuổi, hỗ trợ sinh hoạt hàng ngày, vệ sinh cá nhân, và đồng hành', 'ELDERLY_CARE', 150000, 240, true, NOW(), NOW()),
(2, 'Chăm sóc trẻ em', 'Dịch vụ chăm sóc trẻ em chuyên nghiệp cho mọi lứa tuổi', 'CHILD_CARE', 120000, 240, true, NOW(), NOW()),
(3, 'Chăm sóc bệnh nhân sau phẫu thuật', 'Chăm sóc y tế chuyên nghiệp cho bệnh nhân sau phẫu thuật, bao gồm quản lý thuốc và theo dõi sức khỏe', 'MEDICAL_CARE', 200000, 180, true, NOW(), NOW()),
(4, 'Chăm sóc người khó đi lại', 'Hỗ trợ di chuyển và chăm sóc cho người khó đi lại, người khuyết tật', 'MEDICAL_CARE', 180000, 240, true, NOW(), NOW()),
(5, 'Dịch vụ đồng hành', 'Dịch vụ đồng hành và tương tác xã hội', 'COMPANION', 100000, 180, true, NOW(), NOW()),
(6, 'Dọn dẹp nhà cửa', 'Dịch vụ dọn dẹp nhà cửa nhẹ nhàng và chuẩn bị bữa ăn', 'HOUSEKEEPING', 80000, 180, true, NOW(), NOW()),
(7, 'Chăm sóc điều dưỡng', 'Dịch vụ chăm sóc điều dưỡng chuyên nghiệp', 'NURSING', 250000, 240, true, NOW(), NOW()),
(8, 'Hỗ trợ phục hồi chức năng', 'Hỗ trợ phục hồi chức năng sau chấn thương hoặc phẫu thuật', 'REHABILITATION', 180000, 180, true, NOW(), NOW())
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
