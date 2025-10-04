# API Documentation - Care Service Booking System

## Base URL
```
http://localhost:8080
```

## Authentication
Hầu hết các endpoints đều yêu cầu JWT token trong Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 1. Authentication APIs

### 1.1 Đăng ký Khách hàng
Đăng ký tài khoản Khách hàng để đặt dịch vụ chăm sóc

**Endpoint:** `POST /api/auth/register/customer`

**Request Body:**
```json
{
  "email": "customer@example.com",
  "password": "password123",
  "fullName": "Nguyễn Văn A",
  "phoneNumber": "0901234567",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "emergencyContactName": "Nguyễn Thị B",
  "emergencyContactPhone": "0909876543"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng ký thành công! Bạn có thể đặt dịch vụ ngay bây giờ.",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "customer@example.com",
    "fullName": "Nguyễn Văn A",
    "roles": ["ROLE_CUSTOMER"]
  }
}
```

**Validation:**
- Email: Bắt buộc, phải hợp lệ
- Password: Bắt buộc, tối thiểu 6 ký tự
- Full Name: Bắt buộc, 2-100 ký tự
- Phone Number: Bắt buộc, định dạng: 0xxxxxxxxx hoặc +84xxxxxxxxx

---

### 1.2 Đăng ký Chuyên viên chăm sóc
Đăng ký tài khoản Chuyên viên chăm sóc để cung cấp dịch vụ

**Endpoint:** `POST /api/auth/register/caregiver`

**Request Body:**
```json
{
  "email": "caregiver@example.com",
  "password": "password123",
  "fullName": "Trần Thị C",
  "phoneNumber": "0912345678",
  "address": "456 Đường XYZ, Quận 3, TP.HCM",
  "bio": "Tôi có 5 năm kinh nghiệm chăm sóc người cao tuổi",
  "skills": "Chăm sóc người già, điều dưỡng cơ bản, vật lý trị liệu",
  "yearsOfExperience": 5,
  "idCardNumber": "079123456789",
  "certifications": "Chứng chỉ Điều dưỡng viên, Chứng chỉ Sơ cấp cứu"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng ký thành công! Hồ sơ của bạn đang được xem xét. Chúng tôi sẽ liên hệ sớm.",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 2,
    "email": "caregiver@example.com",
    "fullName": "Trần Thị C",
    "roles": ["ROLE_CAREGIVER"]
  }
}
```

**Validation:**
- Email: Bắt buộc, phải hợp lệ
- Password: Bắt buộc, tối thiểu 6 ký tự
- Full Name: Bắt buộc, 2-100 ký tự
- Phone Number: Bắt buộc, định dạng Việt Nam
- Address: Bắt buộc
- ID Card Number: Bắt buộc, 9-12 chữ số

**Lưu ý:** 
- Tài khoản Chuyên viên sẽ ở trạng thái `PENDING_APPROVAL` 
- Admin cần phê duyệt trước khi Chuyên viên có thể nhận việc

---

### 1.3 Đăng nhập
Đăng nhập vào hệ thống

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "admin@careservice.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@careservice.com",
    "fullName": "System Administrator",
    "roles": ["ROLE_ADMIN"]
  }
}
```

---

### 1.4 Đăng ký (Legacy - Deprecated)
⚠️ **Deprecated**: Sử dụng `/register/customer` hoặc `/register/caregiver` thay thế

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "Nguyen Van A",
  "phoneNumber": "0901234567",
  "address": "123 Street, District, City",
  "role": "CUSTOMER"
}
```

---

## 2. Admin APIs

**Yêu cầu:** Role `ADMIN`

### 2.1 Get Dashboard Statistics
Lấy thống kê tổng quan của hệ thống

**Endpoint:** `GET /api/admin/dashboard/stats`

**Headers:**
```
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "success": true,
  "message": "Dashboard stats retrieved successfully",
  "data": {
    "totalUsers": 100,
    "totalCustomers": 60,
    "totalCaregivers": 35,
    "pendingCaregivers": 5,
    "approvedCaregivers": 30,
    "totalBookings": 150,
    "pendingBookings": 10,
    "completedBookings": 120,
    "cancelledBookings": 20,
    "totalPayments": 120,
    "totalRevenue": 18000000,
    "openTickets": 8,
    "unresolvedTickets": 3
  }
}
```

### 2.2 Get Pending Caregivers
Lấy danh sách caregivers chờ duyệt

**Endpoint:** `GET /api/admin/caregivers/pending`

**Headers:**
```
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "success": true,
  "message": "Pending caregivers retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 4,
      "email": "caregiver@example.com",
      "fullName": "Tran Van C",
      "phoneNumber": "0904234567",
      "verificationStatus": "PENDING",
      "idCardNumber": "079123456789",
      "skills": "Elderly care, Medical care",
      "experience": "5 years",
      "createdAt": "2025-10-04T10:00:00"
    }
  ]
}
```

### 2.3 Approve Caregiver
Duyệt hồ sơ caregiver

**Endpoint:** `PUT /api/admin/caregivers/{id}/approve`

**Headers:**
```
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "success": true,
  "message": "Caregiver approved successfully",
  "data": {
    "id": 1,
    "verificationStatus": "APPROVED",
    "verifiedAt": "2025-10-04T11:00:00"
  }
}
```

### 2.4 Reject Caregiver
Từ chối hồ sơ caregiver

**Endpoint:** `PUT /api/admin/caregivers/{id}/reject`

**Headers:**
```
Authorization: Bearer <admin-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "reason": "Incomplete documents or invalid certification"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Caregiver rejected",
  "data": {
    "id": 1,
    "verificationStatus": "REJECTED",
    "rejectionReason": "Incomplete documents"
  }
}
```

### 2.5 Suspend User
Khóa tài khoản người dùng

**Endpoint:** `PUT /api/admin/users/{id}/suspend`

**Headers:**
```
Authorization: Bearer <admin-token>
```

### 2.6 Activate User
Mở khóa tài khoản người dùng

**Endpoint:** `PUT /api/admin/users/{id}/activate`

**Headers:**
```
Authorization: Bearer <admin-token>
```

---

## 3. Customer APIs

**Yêu cầu:** Role `CUSTOMER` hoặc `ADMIN`

### 3.1 Get Available Caregivers
Xem danh sách caregivers có sẵn

**Endpoint:** `GET /api/customer/caregivers`

**Headers:**
```
Authorization: Bearer <customer-token>
```

**Response:**
```json
{
  "success": true,
  "message": "Available caregivers retrieved successfully",
  "data": [
    {
      "id": 1,
      "fullName": "Tran Thi C",
      "bio": "Experienced caregiver...",
      "skills": "Elderly care, Medical care",
      "hourlyRate": 50000,
      "rating": 4.8,
      "totalReviews": 25,
      "completedBookings": 30,
      "isAvailable": true
    }
  ]
}
```

### 3.2 Create Booking
Tạo booking mới

**Endpoint:** `POST /api/customer/bookings`

**Headers:**
```
Authorization: Bearer <customer-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "serviceId": 1,
  "caregiverId": 1,
  "scheduledStartTime": "2025-10-10T09:00:00",
  "scheduledEndTime": "2025-10-10T13:00:00",
  "location": "123 Nguyen Hue, District 1, HCMC",
  "customerNote": "Please arrive on time"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "id": 1,
    "bookingCode": "BK1728043200123",
    "customerId": 1,
    "customerName": "Nguyen Van A",
    "caregiverId": 1,
    "caregiverName": "Tran Thi C",
    "serviceId": 1,
    "serviceName": "Elderly Care",
    "scheduledStartTime": "2025-10-10T09:00:00",
    "scheduledEndTime": "2025-10-10T13:00:00",
    "totalPrice": 600000,
    "status": "ASSIGNED",
    "location": "123 Nguyen Hue, District 1, HCMC",
    "createdAt": "2025-10-04T10:00:00"
  }
}
```

### 3.3 Get My Bookings
Xem tất cả bookings của mình

**Endpoint:** `GET /api/customer/bookings`

**Headers:**
```
Authorization: Bearer <customer-token>
```

### 3.4 Cancel Booking
Hủy booking

**Endpoint:** `PUT /api/customer/bookings/{id}/cancel`

**Headers:**
```
Authorization: Bearer <customer-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "reason": "Change of plans"
}
```

### 3.5 Create Payment
Tạo thanh toán cho booking

**Endpoint:** `POST /api/customer/payments`

**Headers:**
```
Authorization: Bearer <customer-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "bookingId": 1,
  "paymentMethod": "CREDIT_CARD",
  "notes": "Payment for elderly care service"
}
```

**Payment Methods:**
- `CREDIT_CARD`
- `DEBIT_CARD`
- `BANK_TRANSFER`
- `E_WALLET`
- `CASH`

**Response:**
```json
{
  "success": true,
  "message": "Payment processed successfully",
  "data": {
    "id": 1,
    "transactionId": "TXN1728043200456",
    "bookingId": 1,
    "bookingCode": "BK1728043200123",
    "amount": 600000,
    "paymentMethod": "CREDIT_CARD",
    "status": "COMPLETED",
    "paidAt": "2025-10-04T10:05:00",
    "createdAt": "2025-10-04T10:05:00"
  }
}
```

### 3.6 Create Review
Tạo review cho caregiver

**Endpoint:** `POST /api/customer/reviews`

**Headers:**
```
Authorization: Bearer <customer-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "bookingId": 1,
  "rating": 5,
  "comment": "Excellent service! Very professional and caring."
}
```

**Response:**
```json
{
  "success": true,
  "message": "Review submitted successfully",
  "data": {
    "id": 1,
    "bookingId": 1,
    "customerId": 1,
    "caregiverId": 1,
    "rating": 5,
    "comment": "Excellent service!",
    "createdAt": "2025-10-04T15:00:00"
  }
}
```

### 3.7 Create Support Ticket
Tạo ticket hỗ trợ

**Endpoint:** `POST /api/customer/support/tickets`

**Headers:**
```
Authorization: Bearer <customer-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "category": "BOOKING_ISSUE",
  "priority": "MEDIUM",
  "subject": "Cannot cancel my booking",
  "description": "I'm trying to cancel my booking but getting an error..."
}
```

**Categories:**
- `TECHNICAL_ISSUE`
- `ACCOUNT_ISSUE`
- `BOOKING_ISSUE`
- `PAYMENT_ISSUE`
- `CAREGIVER_VERIFICATION`
- `COMPLAINT`
- `FEEDBACK`
- `OTHER`

**Priorities:**
- `LOW`
- `MEDIUM`
- `HIGH`
- `URGENT`

---

## 4. Caregiver APIs

**Yêu cầu:** Role `CAREGIVER` hoặc `ADMIN`

### 4.1 Get My Profile
Xem profile của mình

**Endpoint:** `GET /api/caregiver/profile`

**Headers:**
```
Authorization: Bearer <caregiver-token>
```

### 4.2 Update Profile
Cập nhật profile

**Endpoint:** `PUT /api/caregiver/profile`

**Headers:**
```
Authorization: Bearer <caregiver-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "bio": "Experienced caregiver with 5 years in elderly care",
  "skills": "Elderly care, Medical care, Companion services",
  "experience": "5 years in professional caregiving",
  "idCardNumber": "079123456789",
  "idCardUrl": "https://example.com/id-card.jpg",
  "certificateUrls": "https://example.com/cert1.jpg,https://example.com/cert2.jpg",
  "availableSchedule": "Mon-Fri: 8AM-6PM, Sat: 9AM-3PM",
  "hourlyRate": 50000
}
```

### 4.3 Update Availability
Cập nhật trạng thái sẵn sàng

**Endpoint:** `PUT /api/caregiver/availability`

**Headers:**
```
Authorization: Bearer <caregiver-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "isAvailable": true
}
```

### 4.4 Get My Bookings
Xem các bookings của mình

**Endpoint:** `GET /api/caregiver/bookings`

**Headers:**
```
Authorization: Bearer <caregiver-token>
```

### 4.5 Accept Booking
Chấp nhận booking

**Endpoint:** `PUT /api/caregiver/bookings/{id}/accept`

**Headers:**
```
Authorization: Bearer <caregiver-token>
```

### 4.6 Reject Booking
Từ chối booking

**Endpoint:** `PUT /api/caregiver/bookings/{id}/reject`

**Headers:**
```
Authorization: Bearer <caregiver-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "reason": "Not available at that time"
}
```

### 4.7 Complete Booking
Hoàn thành booking

**Endpoint:** `PUT /api/caregiver/bookings/{id}/complete`

**Headers:**
```
Authorization: Bearer <caregiver-token>
```

### 4.8 Get My Reviews
Xem các reviews của mình

**Endpoint:** `GET /api/caregiver/reviews`

**Headers:**
```
Authorization: Bearer <caregiver-token>
```

### 4.9 Respond to Review
Trả lời review

**Endpoint:** `PUT /api/caregiver/reviews/{id}/respond`

**Headers:**
```
Authorization: Bearer <caregiver-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "response": "Thank you for your kind words! It was a pleasure working with you."
}
```

---

## 5. Support APIs

**Yêu cầu:** Role `SUPPORT` hoặc `ADMIN`

### 5.1 Get All Tickets
Xem tất cả tickets

**Endpoint:** `GET /api/support/tickets`

**Headers:**
```
Authorization: Bearer <support-token>
```

### 5.2 Get Unassigned Tickets
Xem tickets chưa được gán

**Endpoint:** `GET /api/support/tickets/unassigned`

**Headers:**
```
Authorization: Bearer <support-token>
```

### 5.3 Assign Ticket
Gán ticket cho support agent

**Endpoint:** `PUT /api/support/tickets/{id}/assign`

**Headers:**
```
Authorization: Bearer <support-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "supportUserId": 2
}
```

### 5.4 Update Ticket Status
Cập nhật trạng thái ticket

**Endpoint:** `PUT /api/support/tickets/{id}/status`

**Headers:**
```
Authorization: Bearer <support-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "status": "IN_PROGRESS"
}
```

**Ticket Statuses:**
- `OPEN`
- `IN_PROGRESS`
- `ESCALATED`
- `RESOLVED`
- `CLOSED`
- `REOPENED`

### 5.5 Resolve Ticket
Giải quyết ticket

**Endpoint:** `PUT /api/support/tickets/{id}/resolve`

**Headers:**
```
Authorization: Bearer <support-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "resolution": "Issue has been resolved by resetting the user's password."
}
```

### 5.6 Escalate Ticket
Chuyển ticket lên Admin

**Endpoint:** `PUT /api/support/tickets/{id}/escalate`

**Headers:**
```
Authorization: Bearer <support-token>
```

---

## Error Responses

Tất cả API errors đều trả về format sau:

```json
{
  "success": false,
  "message": "Error message here",
  "timestamp": "2025-10-04T10:00:00"
}
```

### Common Error Codes:
- `400 Bad Request` - Invalid input data
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Status Codes Summary

- `200 OK` - Request successful
- `400 Bad Request` - Invalid request
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error
