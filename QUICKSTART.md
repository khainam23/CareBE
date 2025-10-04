# Quick Start Guide - Care Service Booking System

## Hướng dẫn chạy nhanh

### 1. Cài đặt MySQL

Nếu chưa có MySQL, tải và cài đặt từ: https://dev.mysql.com/downloads/mysql/

### 2. Tạo Database

Mở MySQL Workbench hoặc command line và chạy:

```sql
CREATE DATABASE care_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Cấu hình Database

Mở file `src/main/resources/application.properties` và cập nhật thông tin MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/care_service_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Build và Run

Sử dụng Maven wrapper (khuyến nghị):

**Windows:**
```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Hoặc sử dụng Maven đã cài đặt:
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Test API

Sau khi ứng dụng khởi động thành công, bạn có thể test API tại: `http://localhost:8080`

#### Test đăng nhập Admin:

**Request:**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@careservice.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@careservice.com",
    "fullName": "System Administrator",
    "roles": ["ROLE_ADMIN"]
  },
  "timestamp": "2025-10-04T..."
}
```

#### Test API với JWT Token:

Copy token từ response trên và sử dụng trong header:

```http
GET http://localhost:8080/api/admin/dashboard/stats
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

## Test với các tài khoản mẫu

### 1. Admin Account
- **Email:** admin@careservice.com
- **Password:** admin123
- **Chức năng:** Quản lý toàn bộ hệ thống

### 2. Support Account
- **Email:** support@careservice.com
- **Password:** support123
- **Chức năng:** Xử lý support tickets

### 3. Customer Account
- **Email:** customer@example.com
- **Password:** customer123
- **Chức năng:** Đặt dịch vụ, thanh toán, review

### 4. Caregiver Account
- **Email:** caregiver@example.com
- **Password:** caregiver123
- **Chức năng:** Nhận booking, cung cấp dịch vụ

## Flow Test cơ bản

### 1. Customer đặt dịch vụ

**Bước 1:** Đăng nhập Customer
```http
POST /api/auth/login
{
  "email": "customer@example.com",
  "password": "customer123"
}
```

**Bước 2:** Xem danh sách caregivers
```http
GET /api/customer/caregivers
Authorization: Bearer <customer_token>
```

**Bước 3:** Tạo booking
```http
POST /api/customer/bookings
Authorization: Bearer <customer_token>
{
  "serviceId": 1,
  "caregiverId": 1,
  "scheduledStartTime": "2025-10-10T09:00:00",
  "scheduledEndTime": "2025-10-10T13:00:00",
  "location": "123 Nguyen Hue, District 1, HCMC",
  "customerNote": "Please arrive on time"
}
```

**Bước 4:** Thanh toán
```http
POST /api/customer/payments
Authorization: Bearer <customer_token>
{
  "bookingId": 1,
  "paymentMethod": "CREDIT_CARD"
}
```

### 2. Caregiver xử lý booking

**Bước 1:** Đăng nhập Caregiver
```http
POST /api/auth/login
{
  "email": "caregiver@example.com",
  "password": "caregiver123"
}
```

**Bước 2:** Xem bookings
```http
GET /api/caregiver/bookings
Authorization: Bearer <caregiver_token>
```

**Bước 3:** Chấp nhận booking
```http
PUT /api/caregiver/bookings/1/accept
Authorization: Bearer <caregiver_token>
```

**Bước 4:** Hoàn thành booking
```http
PUT /api/caregiver/bookings/1/complete
Authorization: Bearer <caregiver_token>
```

### 3. Customer review

```http
POST /api/customer/reviews
Authorization: Bearer <customer_token>
{
  "bookingId": 1,
  "rating": 5,
  "comment": "Excellent service!"
}
```

### 4. Admin xem dashboard

```http
GET /api/admin/dashboard/stats
Authorization: Bearer <admin_token>
```

## Troubleshooting

### Lỗi kết nối MySQL
- Kiểm tra MySQL đã chạy: `mysql -u root -p`
- Kiểm tra username/password trong application.properties
- Kiểm tra port MySQL (mặc định 3306)

### Lỗi build Maven
- Đảm bảo Java 17 đã được cài đặt: `java -version`
- Xóa folder target và build lại: `mvn clean install`

### Lỗi JWT Token
- Token có thời hạn 24 giờ, cần login lại khi hết hạn
- Đảm bảo format header đúng: `Authorization: Bearer <token>`

### Lỗi Permission Denied
- Kiểm tra role của user có phù hợp với endpoint không
- Admin có quyền truy cập mọi endpoint
- Customer chỉ truy cập /api/customer/**
- Caregiver chỉ truy cập /api/caregiver/**
- Support truy cập /api/support/**

## Tools hỗ trợ test API

### 1. Postman
- Download: https://www.postman.com/downloads/
- Import collection từ file Postman collection (nếu có)

### 2. Thunder Client (VS Code Extension)
- Cài đặt trong VS Code
- Tạo requests mới và test

### 3. cURL
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@careservice.com","password":"admin123"}'

# Call API with token
curl -X GET http://localhost:8080/api/admin/dashboard/stats \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Next Steps

1. Tích hợp frontend (React, Angular, Vue.js)
2. Thêm file upload cho avatar và certificates
3. Tích hợp payment gateway thực tế (VNPay, Momo, etc.)
4. Thêm email notifications
5. Thêm real-time notifications với WebSocket
6. Deploy lên cloud (AWS, Azure, Google Cloud)

## Liên hệ hỗ trợ

Nếu gặp vấn đề, vui lòng tạo issue trên GitHub hoặc liên hệ team phát triển.
