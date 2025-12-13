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
 '0904234567', '456 Lê Lợi, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
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
 '0905234567', '789 Nguyễn Trãi, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
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
 '0906234567', '321 Võ Văn Tần, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
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
 '0907234567', '654 Lý Tự Trọng, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
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
 '0908234567', '987 Pasteur, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
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
 '0909234567', '147 Trần Hưng Đạo, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (9,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, id_card_number, id_card_url, certificate_urls, verification_status, rejection_reason, is_available, available_schedule, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(6, 9, 'Nhân viên mới, đang chờ xác minh hồ sơ',
 'Elderly care, Companion services',
 '2 năm kinh nghiệm', '079678901234', NULL, NULL,
 'PENDING', NULL, false, NULL, 500000, 0, 0, 0, 0, NOW(), NOW(), NULL)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

---------------------------------------------------
-- MORE DEMO DATA (CUSTOMERS)
---------------------------------------------------

-- Customer 2 (User 10)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(10, 'customer2@example.com', '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6', 'Trần Văn Bình', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0911234567', '123 Lê Duẩn, Q1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (10,3) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO customers (user_id, emergency_contact, emergency_phone, total_spent, booking_count, created_at, updated_at) 
VALUES (10, 'Trần Thị Hoa', '0911234568', 0, 0, NOW(), NOW()) 
ON DUPLICATE KEY UPDATE emergency_contact = VALUES(emergency_contact);

-- Customer 3 (User 11)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(11, 'customer3@example.com', '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6', 'Lê Thị Lan', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0912234567', '456 Nguyễn Thị Minh Khai, Q3, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (11,3) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO customers (user_id, emergency_contact, emergency_phone, total_spent, booking_count, created_at, updated_at) 
VALUES (11, 'Lê Văn Hùng', '0912234568', 0, 0, NOW(), NOW()) 
ON DUPLICATE KEY UPDATE emergency_contact = VALUES(emergency_contact);

-- Customer 4 (User 12)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(12, 'customer4@example.com', '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6', 'Phạm Minh Tuấn', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0913234567', '789 Điện Biên Phủ, Q10, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (12,3) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO customers (user_id, emergency_contact, emergency_phone, total_spent, booking_count, created_at, updated_at) 
VALUES (12, 'Phạm Thị Mai', '0913234568', 0, 0, NOW(), NOW()) 
ON DUPLICATE KEY UPDATE emergency_contact = VALUES(emergency_contact);

-- Customer 5 (User 13)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(13, 'customer5@example.com', '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6', 'Hoàng Thị Mai', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0914234567', '321 Hai Bà Trưng, Q1, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (13,3) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO customers (user_id, emergency_contact, emergency_phone, total_spent, booking_count, created_at, updated_at) 
VALUES (13, 'Hoàng Văn Nam', '0914234568', 0, 0, NOW(), NOW()) 
ON DUPLICATE KEY UPDATE emergency_contact = VALUES(emergency_contact);

-- Customer 6 (User 14)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(14, 'customer6@example.com', '$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6', 'Ngô Văn Hùng', 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg', '0915234567', '654 Cách Mạng Tháng 8, Q10, HCMC', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (14,3) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO customers (user_id, emergency_contact, emergency_phone, total_spent, booking_count, created_at, updated_at) 
VALUES (14, 'Ngô Thị Lan', '0915234568', 0, 0, NOW(), NOW()) 
ON DUPLICATE KEY UPDATE emergency_contact = VALUES(emergency_contact);

---------------------------------------------------
-- MORE DEMO DATA (CAREGIVERS)
---------------------------------------------------

-- Caregiver 7 (User 15)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(15, 'caregiver7@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Đặng Thị Thu', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0916234567', '987 Phạm Văn Đồng, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (15,4) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO caregivers (user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) 
VALUES (15, 'Chuyên gia vật lý trị liệu', 'Rehabilitation, Elderly care', '8 năm kinh nghiệm', 'APPROVED', true, 650000, 4.9, 45, 60, 39000000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE bio = VALUES(bio);

-- Caregiver 8 (User 16)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(16, 'caregiver8@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Bùi Văn Long', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0917234567', '147 Hoàng Văn Thụ, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (16,4) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO caregivers (user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) 
VALUES (16, 'Chăm sóc người bệnh tại nhà', 'Medical care, Nursing', '5 năm kinh nghiệm', 'APPROVED', true, 550000, 4.7, 28, 35, 19250000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE bio = VALUES(bio);

-- Caregiver 9 (User 17)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(17, 'caregiver9@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Đỗ Thị Tuyết', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0918234567', '258 Phan Đăng Lưu, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);
INSERT INTO user_roles VALUES (17,4) ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
INSERT INTO caregivers (user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) 
VALUES (17, 'Chăm sóc mẹ và bé', 'Child care, Postpartum care', '4 năm kinh nghiệm', 'APPROVED', true, 500000, 4.8, 30, 40, 20000000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE bio = VALUES(bio);

---------------------------------------------------
-- MORE DEMO DATA (BOOKINGS)
---------------------------------------------------

INSERT INTO bookings (booking_code, customer_id, caregiver_id, service_id, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, total_price, status, location, created_at, updated_at) VALUES
('BK001', (SELECT id FROM customers WHERE user_id=3), (SELECT id FROM caregivers WHERE user_id=4), 1, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 5 DAY), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 5 DAY), INTERVAL 4 HOUR), 600000, 'COMPLETED', '123 Nguyễn Huệ, Q1, HCMC', DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
('BK002', (SELECT id FROM customers WHERE user_id=10), (SELECT id FROM caregivers WHERE user_id=5), 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 3 DAY), INTERVAL 4 HOUR), 480000, 'COMPLETED', '123 Lê Duẩn, Q1, HCMC', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
('BK003', (SELECT id FROM customers WHERE user_id=11), (SELECT id FROM caregivers WHERE user_id=6), 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 3 HOUR), 600000, 'COMPLETED', '456 Nguyễn Thị Minh Khai, Q3, HCMC', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
('BK004', (SELECT id FROM customers WHERE user_id=12), (SELECT id FROM caregivers WHERE user_id=15), 4, NOW() + INTERVAL 1 DAY, NOW() + INTERVAL 1 DAY + INTERVAL 4 HOUR, NULL, NULL, 720000, 'CONFIRMED', '789 Điện Biên Phủ, Q10, HCMC', NOW(), NOW()),
('BK005', (SELECT id FROM customers WHERE user_id=13), (SELECT id FROM caregivers WHERE user_id=16), 5, NOW() + INTERVAL 2 DAY, NOW() + INTERVAL 2 DAY + INTERVAL 3 HOUR, NULL, NULL, 300000, 'PENDING', '321 Hai Bà Trưng, Q1, HCMC', NOW(), NOW()),
('BK006', (SELECT id FROM customers WHERE user_id=3), (SELECT id FROM caregivers WHERE user_id=5), 6, NOW() + INTERVAL 3 DAY, NOW() + INTERVAL 3 DAY + INTERVAL 3 HOUR, NULL, NULL, 240000, 'PENDING', '123 Nguyễn Huệ, Q1, HCMC', NOW(), NOW()),
('BK007', (SELECT id FROM customers WHERE user_id=10), (SELECT id FROM caregivers WHERE user_id=6), 7, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 10 DAY), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 10 DAY), INTERVAL 4 HOUR), 1000000, 'COMPLETED', '123 Lê Duẩn, Q1, HCMC', DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),
('BK008', (SELECT id FROM customers WHERE user_id=11), (SELECT id FROM caregivers WHERE user_id=17), 8, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 3 HOUR), NULL, NULL, 540000, 'CANCELLED', '456 Nguyễn Thị Minh Khai, Q3, HCMC', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
('BK009', (SELECT id FROM customers WHERE user_id=12), (SELECT id FROM caregivers WHERE user_id=4), 1, NOW() + INTERVAL 5 DAY, NOW() + INTERVAL 5 DAY + INTERVAL 4 HOUR, NULL, NULL, 600000, 'CONFIRMED', '789 Điện Biên Phủ, Q10, HCMC', NOW(), NOW()),
('BK010', (SELECT id FROM customers WHERE user_id=13), (SELECT id FROM caregivers WHERE user_id=5), 2, NOW() + INTERVAL 6 DAY, NOW() + INTERVAL 6 DAY + INTERVAL 4 HOUR, NULL, NULL, 480000, 'PENDING', '321 Hai Bà Trưng, Q1, HCMC', NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status);

---------------------------------------------------
-- MORE DEMO DATA (PAYMENTS)
---------------------------------------------------

INSERT INTO payments (transaction_id, booking_id, amount, payment_method, status, paid_at, created_at, updated_at) VALUES
('TXN001', (SELECT id FROM bookings WHERE booking_code='BK001'), 600000, 'CREDIT_CARD', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
('TXN002', (SELECT id FROM bookings WHERE booking_code='BK002'), 480000, 'E_WALLET', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
('TXN003', (SELECT id FROM bookings WHERE booking_code='BK003'), 600000, 'CASH', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
('TXN004', (SELECT id FROM bookings WHERE booking_code='BK007'), 1000000, 'BANK_TRANSFER', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY))
ON DUPLICATE KEY UPDATE status = VALUES(status);

---------------------------------------------------
-- MORE DEMO DATA (REVIEWS)
---------------------------------------------------

INSERT INTO reviews (booking_id, customer_id, caregiver_id, rating, comment, created_at) VALUES
((SELECT id FROM bookings WHERE booking_code='BK001'), (SELECT id FROM customers WHERE user_id=3), (SELECT id FROM caregivers WHERE user_id=4), 5, 'Dịch vụ rất tốt, cô Cúc rất tận tâm.', DATE_SUB(NOW(), INTERVAL 4 DAY)),
((SELECT id FROM bookings WHERE booking_code='BK002'), (SELECT id FROM customers WHERE user_id=10), (SELECT id FROM caregivers WHERE user_id=5), 4, 'Chăm sóc trẻ tốt, nhưng đến hơi muộn một chút.', DATE_SUB(NOW(), INTERVAL 2 DAY)),
((SELECT id FROM bookings WHERE booking_code='BK003'), (SELECT id FROM customers WHERE user_id=11), (SELECT id FROM caregivers WHERE user_id=6), 5, 'Y tá Hoài rất chuyên nghiệp, tôi rất hài lòng.', NOW()),
((SELECT id FROM bookings WHERE booking_code='BK007'), (SELECT id FROM customers WHERE user_id=10), (SELECT id FROM caregivers WHERE user_id=6), 5, 'Tuyệt vời, sẽ đặt lại.', DATE_SUB(NOW(), INTERVAL 9 DAY))
ON DUPLICATE KEY UPDATE rating = VALUES(rating);

---------------------------------------------------
-- ADDITIONAL 50 CAREGIVERS (10 per district)
---------------------------------------------------

-- QUẬN HẢI CHÂU (10 caregivers)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(18, 'caregiver10@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Nguyễn Thị Hương', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0920101001', '123 Bạch Đằng, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(19, 'caregiver11@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Trần Văn Hạnh', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0920101002', '456 Hải Phòng, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(20, 'caregiver12@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Phạm Thị Linh', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920101003', '789 Trần Phú, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(21, 'caregiver13@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Lê Văn Kiên', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0920101004', '321 Nguyễn Lương Bằng, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(22, 'caregiver14@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Đặng Thị Mai', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920101005', '654 Đinh Tiên Hoàng, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(23, 'caregiver15@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hoàng Văn Sơn', 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg', '0920101006', '147 Quang Trung, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(24, 'caregiver16@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Vũ Thị Hồng', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920101007', '258 Thạch Thị Thanh, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(25, 'caregiver17@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Ngô Văn Long', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920101008', '369 Huỳnh Thúc Kháng, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(26, 'caregiver18@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tô Thị Thúy', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0920101009', '741 Nguyễn Chí Thanh, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(27, 'caregiver19@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Đỗ Văn Minh', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0920101010', '852 Tôn Thất Tùng, Quận Hải Châu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (18,4), (19,4), (20,4), (21,4), (22,4), (23,4), (24,4), (25,4), (26,4), (27,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(7, 18, 'Chuyên chăm sóc người già', 'Elderly care, Companion services', '3 năm kinh nghiệm', 'APPROVED', true, 480000, 4.5, 12, 15, 7200000, NOW(), NOW(), NOW()),
(8, 19, 'Chăm sóc trẻ em và giúp việc nhà', 'Child care, Housekeeping', '4 năm kinh nghiệm', 'APPROVED', true, 520000, 4.6, 16, 20, 10400000, NOW(), NOW(), NOW()),
(9, 20, 'Y tá chuyên nghiệp', 'Medical care, Nursing', '6 năm kinh nghiệm', 'APPROVED', true, 650000, 4.8, 28, 35, 22750000, NOW(), NOW(), NOW()),
(10, 21, 'Chăm sóc đa năng', 'Elderly care, Child care, Companion', '2 năm kinh nghiệm', 'APPROVED', true, 450000, 4.4, 10, 12, 5400000, NOW(), NOW(), NOW()),
(11, 22, 'Hỗ trợ phục hồi chức năng', 'Rehabilitation, Medical care', '5 năm kinh nghiệm', 'APPROVED', true, 600000, 4.7, 22, 28, 16800000, NOW(), NOW(), NOW()),
(12, 23, 'Chuyên giáo dục trẻ', 'Child care, Education support', '3 năm kinh nghiệm', 'APPROVED', true, 500000, 4.5, 14, 18, 9000000, NOW(), NOW(), NOW()),
(13, 24, 'Điều dưỡng tại nhà', 'Nursing, Medical care', '7 năm kinh nghiệm', 'APPROVED', true, 700000, 4.9, 35, 45, 31500000, NOW(), NOW(), NOW()),
(14, 25, 'Giúp việc nhà', 'Housekeeping, Companion', '2 năm kinh nghiệm', 'APPROVED', true, 400000, 4.3, 8, 10, 4000000, NOW(), NOW(), NOW()),
(15, 26, 'Chăm sóc mẹ và bé', 'Child care, Postpartum care', '3 năm kinh nghiệm', 'APPROVED', true, 550000, 4.6, 18, 22, 12100000, NOW(), NOW(), NOW()),
(16, 27, 'Hỗ trợ người cao tuổi', 'Elderly care, Companion services', '4 năm kinh nghiệm', 'APPROVED', true, 530000, 4.5, 15, 19, 10070000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- QUẬN THANH KHÊ (10 caregivers)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(28, 'caregiver20@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Lý Thị Thanh', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920201001', '123 Triệu Nữ Vương, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(29, 'caregiver21@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Phan Văn Toàn', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920201002', '456 Tôn Đức Thắng, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(30, 'caregiver22@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Võ Thị Hương', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920201003', '789 Văn Tố, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(31, 'caregiver23@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Bùi Văn Hùng', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0920201004', '321 Phan Bội Châu, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(32, 'caregiver24@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Trần Thị Trang', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0920201005', '654 Ngô Quyền, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(33, 'caregiver25@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hồ Văn Huy', 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg', '0920201006', '147 Lê Duẩn, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(34, 'caregiver26@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Chu Thị Duyên', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920201007', '258 Hàng Bài, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(35, 'caregiver27@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Dương Văn Phát', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0920201008', '369 Hùng Vương, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(36, 'caregiver28@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Nguyễn Thị Kiều', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920201009', '741 Tự Do, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(37, 'caregiver29@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tạ Văn Minh', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920201010', '852 Ngũ Hành Sơn, Quận Thanh Khê, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (28,4), (29,4), (30,4), (31,4), (32,4), (33,4), (34,4), (35,4), (36,4), (37,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(17, 28, 'Chuyên chăm sóc người bệnh', 'Medical care, Nursing', '5 năm kinh nghiệm', 'APPROVED', true, 620000, 4.7, 20, 26, 16120000, NOW(), NOW(), NOW()),
(18, 29, 'Chăm sóc trẻ sơ sinh', 'Child care, Postpartum care', '3 năm kinh nghiệm', 'APPROVED', true, 560000, 4.6, 17, 21, 11760000, NOW(), NOW(), NOW()),
(19, 30, 'Hỗ trợ người khuyết tật', 'Medical care, Rehabilitation', '4 năm kinh nghiệm', 'APPROVED', true, 580000, 4.5, 15, 19, 11020000, NOW(), NOW(), NOW()),
(20, 31, 'Giáo dục và chăm sóc trẻ', 'Child care, Education', '2 năm kinh nghiệm', 'APPROVED', true, 490000, 4.4, 12, 15, 7350000, NOW(), NOW(), NOW()),
(21, 32, 'Điều dưỡng chuyên nghiệp', 'Nursing, Medical care', '6 năm kinh nghiệm', 'APPROVED', true, 680000, 4.8, 25, 32, 21760000, NOW(), NOW(), NOW()),
(22, 33, 'Hỗ trợ sinh hoạt', 'Elderly care, Companion', '3 năm kinh nghiệm', 'APPROVED', true, 470000, 4.4, 13, 16, 7520000, NOW(), NOW(), NOW()),
(23, 34, 'Chăm sóc toàn diện', 'Elderly care, Child care, Housekeeping', '5 năm kinh nghiệm', 'APPROVED', true, 590000, 4.6, 19, 24, 14160000, NOW(), NOW(), NOW()),
(24, 35, 'Phục hồi chức năng', 'Rehabilitation, Physical therapy', '4 năm kinh nghiệm', 'APPROVED', true, 610000, 4.7, 21, 27, 16497000, NOW(), NOW(), NOW()),
(25, 36, 'Chăm sóc và giáo dục', 'Child care, Education support', '3 năm kinh nghiệm', 'APPROVED', true, 520000, 4.5, 14, 18, 9360000, NOW(), NOW(), NOW()),
(26, 37, 'Hỗ trợ y tế tại nhà', 'Medical care, Nursing care', '5 năm kinh nghiệm', 'APPROVED', true, 640000, 4.7, 23, 30, 19200000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- QUẬN SƠN TRÀ (10 caregivers)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(38, 'caregiver30@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Ương Thị Hồng', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0920301001', '123 Bùi Tấn, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(39, 'caregiver31@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Trương Văn Sơn', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0920301002', '456 Khuê Mỹ, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(40, 'caregiver32@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Lê Thị Vân', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920301003', '789 Nại Hiên, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(41, 'caregiver33@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Phạm Văn Hùng', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920301004', '321 An Thượng, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(42, 'caregiver34@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Ngô Thị Mạnh', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920301005', '654 Mân Thái, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(43, 'caregiver35@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tô Văn Tuấn', 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg', '0920301006', '147 Thọ Quang, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(44, 'caregiver36@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hứa Thị Xuân', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0920301007', '258 Trần Hưng Đạo, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(45, 'caregiver37@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Lý Văn Hải', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920301008', '369 Nước Man, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(46, 'caregiver38@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Cao Thị Huyền', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920301009', '741 Bình Định, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(47, 'caregiver39@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Kiều Văn Mạnh', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920301010', '852 Khuê Đông, Quận Sơn Trà, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (38,4), (39,4), (40,4), (41,4), (42,4), (43,4), (44,4), (45,4), (46,4), (47,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(27, 38, 'Chăm sóc người lớn tuổi', 'Elderly care, Companion services', '4 năm kinh nghiệm', 'APPROVED', true, 540000, 4.6, 16, 20, 10800000, NOW(), NOW(), NOW()),
(28, 39, 'Y tá bệnh viện', 'Nursing, Medical care', '6 năm kinh nghiệm', 'APPROVED', true, 670000, 4.8, 26, 33, 22110000, NOW(), NOW(), NOW()),
(29, 40, 'Chăm sóc trẻ mầm non', 'Child care, Education', '3 năm kinh nghiệm', 'APPROVED', true, 510000, 4.5, 14, 17, 8670000, NOW(), NOW(), NOW()),
(30, 41, 'Hỗ trợ phục hồi', 'Rehabilitation, Physical therapy', '5 năm kinh nghiệm', 'APPROVED', true, 600000, 4.7, 21, 27, 16200000, NOW(), NOW(), NOW()),
(31, 42, 'Điều dưỡng tại gia', 'Nursing, Medical care', '7 năm kinh nghiệm', 'APPROVED', true, 720000, 4.9, 32, 41, 29520000, NOW(), NOW(), NOW()),
(32, 43, 'Giúp việc nhà và chăm sóc', 'Housekeeping, Companion', '2 năm kinh nghiệm', 'APPROVED', true, 430000, 4.3, 9, 11, 4730000, NOW(), NOW(), NOW()),
(33, 44, 'Chăm sóc mẹ sau sinh', 'Postpartum care, Child care', '3 năm kinh nghiệm', 'APPROVED', true, 570000, 4.6, 18, 23, 13110000, NOW(), NOW(), NOW()),
(34, 45, 'Hỗ trợ người khuyết tật', 'Medical care, Rehabilitation', '4 năm kinh nghiệm', 'APPROVED', true, 590000, 4.6, 17, 22, 12980000, NOW(), NOW(), NOW()),
(35, 46, 'Chăm sóc đa dạng', 'Elderly care, Child care', '3 năm kinh nghiệm', 'APPROVED', true, 550000, 4.5, 15, 19, 10450000, NOW(), NOW(), NOW()),
(36, 47, 'Y tế cơ bản tại nhà', 'Medical care, Nursing', '5 năm kinh nghiệm', 'APPROVED', true, 650000, 4.7, 22, 28, 18200000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- QUẬN NGŨ HÀNH SƠN (10 caregivers)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(48, 'caregiver40@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Vũ Thị Loan', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920401001', '123 Bình Dương, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(49, 'caregiver41@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hoàng Văn Tiến', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0920401002', '456 Hòa Phát, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(50, 'caregiver42@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Đoàn Thị Hoa', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0920401003', '789 Khuê Trung, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(51, 'caregiver43@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Trịnh Văn Quyết', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920401004', '321 Thế Lữ, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(52, 'caregiver44@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Bế Thị Hà', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0920401005', '654 Tây Lộc, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(53, 'caregiver45@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Nguyễn Văn Cường', 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg', '0920401006', '147 Hòa Sơn, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(54, 'caregiver46@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Phạm Thị Anh', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920401007', '258 Trần Hưng Đạo, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(55, 'caregiver47@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tôn Văn Huy', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920401008', '369 Khuê Mỹ, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(56, 'caregiver48@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Mạc Thị Hồng', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920401009', '741 Mân Thái, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(57, 'caregiver49@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Quách Văn Hùng', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0920401010', '852 Khuê Đông, Quận Ngũ Hành Sơn, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (48,4), (49,4), (50,4), (51,4), (52,4), (53,4), (54,4), (55,4), (56,4), (57,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(37, 48, 'Chuyên chăm sóc người lớn', 'Elderly care, Companion', '4 năm kinh nghiệm', 'APPROVED', true, 560000, 4.6, 17, 21, 11760000, NOW(), NOW(), NOW()),
(38, 49, 'Điều dưỡng bệnh viện', 'Nursing, Medical care', '6 năm kinh nghiệm', 'APPROVED', true, 690000, 4.8, 27, 35, 24150000, NOW(), NOW(), NOW()),
(39, 50, 'Chăm sóc trẻ em toàn diện', 'Child care, Education', '3 năm kinh nghiệm', 'APPROVED', true, 530000, 4.5, 15, 19, 10070000, NOW(), NOW(), NOW()),
(40, 51, 'Phục hồi chức năng chuyên nghiệp', 'Rehabilitation, Physical therapy', '5 năm kinh nghiệm', 'APPROVED', true, 620000, 4.7, 22, 28, 17360000, NOW(), NOW(), NOW()),
(41, 52, 'Y tá tại nhà', 'Nursing, Medical care', '7 năm kinh nghiệm', 'APPROVED', true, 740000, 4.9, 33, 43, 31820000, NOW(), NOW(), NOW()),
(42, 53, 'Giúp việc nhà', 'Housekeeping, Companion', '2 năm kinh nghiệm', 'APPROVED', true, 450000, 4.4, 11, 14, 6300000, NOW(), NOW(), NOW()),
(43, 54, 'Chăm sóc sau sinh', 'Postpartum care, Child care', '3 năm kinh nghiệm', 'APPROVED', true, 590000, 4.6, 19, 24, 14160000, NOW(), NOW(), NOW()),
(44, 55, 'Hỗ trợ y tế tại nhà', 'Medical care, Rehabilitation', '4 năm kinh nghiệm', 'APPROVED', true, 610000, 4.6, 18, 23, 14030000, NOW(), NOW(), NOW()),
(45, 56, 'Chăm sóc kép', 'Elderly care, Child care', '3 năm kinh nghiệm', 'APPROVED', true, 570000, 4.5, 16, 20, 11400000, NOW(), NOW(), NOW()),
(46, 57, 'Y tế gia đình', 'Medical care, Nursing', '5 năm kinh nghiệm', 'APPROVED', true, 670000, 4.7, 23, 29, 19430000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- QUẬN LIÊN CHIỂU (10 caregivers)
INSERT INTO users (id, email, password, full_name, avatar_url, phone_number, address, status, enabled, created_at, updated_at) VALUES
(58, 'caregiver50@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Lương Thị Thảo', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920501001', '123 Hòa Thuận, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(59, 'caregiver51@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Đinh Văn Lâm', 'https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg', '0920501002', '456 Hòa Minh, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(60, 'caregiver52@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hồ Thị Thanh', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920501003', '789 Hòa Cường, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(61, 'caregiver53@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tạ Văn Dũng', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920501004', '321 Hòa Khánh, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(62, 'caregiver54@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Ngô Thị Mỹ', 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg', '0920501005', '654 Hòa Phát, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(63, 'caregiver55@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Kiều Văn Sơn', 'https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg', '0920501006', '147 Hòa Vượng, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(64, 'caregiver56@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Hà Thị Hương', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg', '0920501007', '258 Hòa Tân, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(65, 'caregiver57@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Cao Văn Minh', 'https://images.pexels.com/photos/532758/pexels-photo-532758.jpeg', '0920501008', '369 Hòa Xuân, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(66, 'caregiver58@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Tôn Thị Linh', 'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', '0920501009', '741 Hòa Hải, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW()),
(67, 'caregiver59@example.com', '$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O', 'Vân Văn Tuấn', 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg', '0920501010', '852 Hòa Quý, Quận Liên Chiểu, Đà Nẵng', 'ACTIVE', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_roles VALUES (58,4), (59,4), (60,4), (61,4), (62,4), (63,4), (64,4), (65,4), (66,4), (67,4)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO caregivers (id, user_id, bio, skills, experience, verification_status, is_available, hourly_rate, rating, total_reviews, completed_bookings, total_earnings, created_at, updated_at, verified_at) VALUES
(47, 58, 'Chăm sóc người cao tuổi', 'Elderly care, Companion', '4 năm kinh nghiệm', 'APPROVED', true, 550000, 4.6, 17, 21, 11550000, NOW(), NOW(), NOW()),
(48, 59, 'Điều dưỡng chuyên nghiệp', 'Nursing, Medical care', '6 năm kinh nghiệm', 'APPROVED', true, 710000, 4.8, 28, 36, 25560000, NOW(), NOW(), NOW()),
(49, 60, 'Chăm sóc trẻ sơ sinh', 'Child care, Postpartum care', '3 năm kinh nghiệm', 'APPROVED', true, 540000, 4.5, 14, 18, 9720000, NOW(), NOW(), NOW()),
(50, 61, 'Phục hồi chức năng', 'Rehabilitation, Physical therapy', '5 năm kinh nghiệm', 'APPROVED', true, 630000, 4.7, 23, 29, 18270000, NOW(), NOW(), NOW()),
(51, 62, 'Y tá bệnh viện', 'Nursing, Medical care', '7 năm kinh nghiệm', 'APPROVED', true, 750000, 4.9, 34, 44, 33000000, NOW(), NOW(), NOW()),
(52, 63, 'Hỗ trợ sinh hoạt', 'Housekeeping, Companion', '2 năm kinh nghiệm', 'APPROVED', true, 460000, 4.4, 12, 15, 6900000, NOW(), NOW(), NOW()),
(53, 64, 'Chăm sóc mẹ và bé', 'Postpartum care, Child care', '3 năm kinh nghiệm', 'APPROVED', true, 600000, 4.6, 20, 25, 15000000, NOW(), NOW(), NOW()),
(54, 65, 'Hỗ trợ y tế', 'Medical care, Rehabilitation', '4 năm kinh nghiệm', 'APPROVED', true, 620000, 4.6, 19, 24, 14880000, NOW(), NOW(), NOW()),
(55, 66, 'Chăm sóc đa năng', 'Elderly care, Child care', '3 năm kinh nghiệm', 'APPROVED', true, 580000, 4.5, 16, 20, 11600000, NOW(), NOW(), NOW()),
(56, 67, 'Y tế cơ bản', 'Medical care, Nursing', '5 năm kinh nghiệm', 'APPROVED', true, 680000, 4.7, 24, 30, 20400000, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
