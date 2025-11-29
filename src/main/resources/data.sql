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
(6, 'Dọn dẹp nhà cửa', 'Dịch vụ dọn dẹp nhà cửa nhẹ nhàng và chuẩn bị bữa ăn', 'HOUSEKEEPING', 800000, 180, true, NOW(), NOW()),
(7, 'Chăm sóc điều dưỡng', 'Dịch vụ chăm sóc điều dưỡng chuyên nghiệp', 'NURSING', 250000, 240, true, NOW(), NOW()),
(8, 'Hỗ trợ phục hồi chức năng', 'Hỗ trợ phục hồi chức năng sau chấn thương hoặc phẫu thuật', 'REHABILITATION', 180000, 180, true, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

---------------------------------------------------
-- USERS + AVATAR (REAL PEOPLE STOCK IMAGES)
---------------------------------------------------

-- Admin
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(1, 'admin@careservice.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Trần Quốc Khánh',
 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg',
 '0901234567', 'Hồ Chí Minh', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (1,1)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Support
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(2, 'support@careservice.com',
 '$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW',
 'Lê Thị Minh Thảo',
 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg',
 '0902234567', 'Hồ Chí Minh', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (2,2)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Customer
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(3, 'customer@example.com',
 '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6',
 'Nguyễn Minh An',
 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg',
 '0903234567', '123 Nguyễn Huệ, Q1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (3,3)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO customers (id, user_id, emergency_contact, emergency_phone, special_requirements, preferred_location, total_spent, booking_count, created_at, updated_at) VALUES
(1, 3, 'Nguyễn Thị Bình', '0904234567', NULL, NULL, 0, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

---------------------------------------------------
-- CAREGIVERS (REAL NAMES + REAL STOCK IMAGES)
---------------------------------------------------

-- Caregiver 1
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(4, 'caregiver@example.com',
 '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O',
 'Trần Thị Cúc',
 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg',
 '0904234567', '456 Lê Lợi, Q3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (4,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(1, 4, 'Chuyên viên chăm sóc người già 5 năm kinh nghiệm',
 'Elderly care, Medical care, Companion services',
 '5 năm kinh nghiệm', '079123456789', NULL, NULL,
 'APPROVED', NULL, true, NULL, 505000, 4.8, 25, 30, 15000000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 2
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(5, 'caregiver2@example.com',
 '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O',
 'Lê Văn Dũng',
 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg',
 '0905234567', '789 Nguyễn Trãi, Q5, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (5,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(2, 5, 'Chuyên chăm sóc trẻ em 3 năm kinh nghiệm',
 'Child care, Housekeeping, Companion services',
 '3 năm kinh nghiệm', '079234567890', NULL, NULL,
 'APPROVED', NULL, true, NULL, 450000, 4.6, 18, 22, 9800000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 3
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(6, 'caregiver3@example.com',
 '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O',
 'Phạm Thị Hoài',
 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg',
 '0906234567', '321 Võ Văn Tần, Q3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (6,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(3, 6, 'Y tá 7 năm kinh nghiệm chăm sóc y tế tại nhà',
 'Medical care, Nursing care, Rehabilitation support',
 '7 năm điều dưỡng', '079345678901', NULL, NULL,
 'APPROVED', NULL, true, NULL, 700000, 4.9, 42, 55, 38500000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 4
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(7, 'caregiver4@example.com',
 '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O',
 'Hoàng Văn Phúc',
 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg',
 '0907234567', '654 Lý Tự Trọng, Q1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (7,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(4, 7, 'Chuyên chăm sóc người cao tuổi và phục hồi chức năng',
 'Elderly care, Rehabilitation support, Companion services',
 '4 năm kinh nghiệm', '079456789012', NULL, NULL,
 'APPROVED', NULL, true, NULL, 550000, 4.7, 32, 40, 22000000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 5
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(8, 'caregiver5@example.com',
 '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O',
 'Trần Thị Gia Hân',
 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg',
 '0908234567', '987 Pasteur, Q3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (8,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(5, 8, 'Chuyên viên chăm sóc đa năng 6 năm kinh nghiệm',
 'Elderly care, Child care, Housekeeping, Companion services',
 '6 năm kinh nghiệm', '079567890123', NULL, NULL,
 'APPROVED', NULL, true, NULL, 600000, 4.8, 38, 48, 28800000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Caregiver 6 (Pending)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(9, 'caregiver6@example.com',
 '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O',
 'Nguyễn Văn Hải',
 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg',
 '0909234567', '147 Trần Hưng Đạo, Q1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (9,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(6, 9, 'Nhân viên mới, đang chờ xác minh hồ sơ',
 'Elderly care, Companion services',
 '2 năm kinh nghiệm', '079678901234', NULL, NULL,
 'PENDING', NULL, false, NULL, 500000, 0, 0, 0, 0, NOW(), NOW(), NULL)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
