# Care Service Booking System - Backend

Hệ thống backend cho ứng dụng đặt dịch vụ chăm sóc được xây dựng bằng Spring Boot 3, Java 17, và MySQL.

## Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security + JWT Authentication**
- **Spring Data JPA + Hibernate**
- **MySQL Database**
- **Maven**
- **Lombok**

## Cấu trúc dự án

```
src/main/java/com/careservice/
├── config/              # Cấu hình Spring Security
├── controller/          # REST API Controllers
│   ├── AdminController
│   ├── AuthController
│   ├── CaregiverController
│   ├── CustomerController
│   └── SupportController
├── dto/                 # Data Transfer Objects
│   ├── admin/
│   ├── auth/
│   ├── booking/
│   ├── caregiver/
│   ├── payment/
│   ├── review/
│   └── support/
├── entity/              # JPA Entities
├── exception/           # Exception Handlers
├── repository/          # JPA Repositories
├── security/            # JWT & Security components
└── service/             # Business Logic Layer
```

## Các tính năng chính

### 1. Xác thực & Phân quyền (Authentication & Authorization)
- JWT-based authentication
- 4 vai trò: Admin, Support, Customer, Caregiver
- Spring Security với role-based access control

### 2. Quản lý Admin
- Dashboard với thống kê tổng quan
- Duyệt/từ chối caregiver
- Quản lý người dùng (khóa/mở khóa tài khoản)
- Xem báo cáo hệ thống

### 3. Quản lý Support
- Tiếp nhận và xử lý tickets hỗ trợ
- Gán tickets cho support agents
- Cập nhật trạng thái tickets
- Giải quyết và đóng tickets
- Escalate tickets lên Admin

### 4. Chức năng Customer
- Đăng ký và đăng nhập
- Tìm kiếm caregivers
- Đặt dịch vụ chăm sóc
- Thanh toán trực tuyến
- Hủy booking
- Đánh giá và review sau khi sử dụng dịch vụ
- Tạo support tickets

### 5. Chức năng Caregiver
- Đăng ký và nộp hồ sơ xác minh
- Cập nhật profile và kỹ năng
- Quản lý lịch làm việc
- Chấp nhận/từ chối booking
- Hoàn thành booking
- Xem đánh giá từ khách hàng
- Trả lời reviews
- Theo dõi thu nhập

## Cài đặt và Chạy

### Yêu cầu
- JDK 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+

### Bước 1: Clone repository
```bash
git clone <repository-url>
cd Care
```

### Bước 2: Cấu hình MySQL
Tạo database trong MySQL:
```sql
CREATE DATABASE care_service_db;
```

Cập nhật thông tin kết nối trong `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/care_service_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Bước 3: Build project
```bash
mvn clean install
```

### Bước 4: Chạy ứng dụng
```bash
mvn spring-boot:run
```

Hoặc chạy file JAR:
```bash
java -jar target/care-booking-system-1.0.0.jar
```

Ứng dụng sẽ chạy trên: `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Đăng ký tài khoản
- `POST /api/auth/login` - Đăng nhập

### Admin APIs
- `GET /api/admin/dashboard/stats` - Xem thống kê dashboard
- `GET /api/admin/caregivers/pending` - Danh sách caregivers chờ duyệt
- `PUT /api/admin/caregivers/{id}/approve` - Duyệt caregiver
- `PUT /api/admin/caregivers/{id}/reject` - Từ chối caregiver
- `PUT /api/admin/users/{id}/suspend` - Khóa tài khoản
- `PUT /api/admin/users/{id}/activate` - Mở khóa tài khoản

### Support APIs
- `GET /api/support/tickets` - Xem tất cả tickets
- `GET /api/support/tickets/unassigned` - Tickets chưa được gán
- `GET /api/support/tickets/assigned` - Tickets đã được gán
- `PUT /api/support/tickets/{id}/assign` - Gán ticket
- `PUT /api/support/tickets/{id}/resolve` - Giải quyết ticket
- `PUT /api/support/tickets/{id}/escalate` - Escalate ticket

### Customer APIs
- `POST /api/customer/bookings` - Tạo booking mới
- `GET /api/customer/bookings` - Xem bookings của mình
- `PUT /api/customer/bookings/{id}/cancel` - Hủy booking
- `POST /api/customer/payments` - Thanh toán
- `POST /api/customer/reviews` - Tạo review
- `GET /api/customer/caregivers` - Xem danh sách caregivers
- `POST /api/customer/support/tickets` - Tạo support ticket

### Caregiver APIs
- `GET /api/caregiver/profile` - Xem profile
- `PUT /api/caregiver/profile` - Cập nhật profile
- `PUT /api/caregiver/availability` - Cập nhật trạng thái sẵn sàng
- `GET /api/caregiver/bookings` - Xem bookings
- `PUT /api/caregiver/bookings/{id}/accept` - Chấp nhận booking
- `PUT /api/caregiver/bookings/{id}/reject` - Từ chối booking
- `PUT /api/caregiver/bookings/{id}/complete` - Hoàn thành booking
- `GET /api/caregiver/reviews` - Xem reviews
- `PUT /api/caregiver/reviews/{id}/respond` - Trả lời review

## Xác thực API

Tất cả các API (trừ `/api/auth/**`) đều yêu cầu JWT token trong header:

```
Authorization: Bearer <your-jwt-token>
```

## Tài khoản mẫu

Sau khi chạy ứng dụng, các tài khoản mẫu sẽ được tạo:

1. **Admin**
   - Email: admin@careservice.com
   - Password: admin123

2. **Support**
   - Email: support@careservice.com
   - Password: support123

3. **Customer**
   - Email: customer@example.com
   - Password: customer123

4. **Caregiver**
   - Email: caregiver@example.com
   - Password: caregiver123

## Database Schema

Hệ thống bao gồm các bảng chính:
- `users` - Thông tin người dùng
- `roles` - Vai trò trong hệ thống
- `customers` - Profile khách hàng
- `caregivers` - Profile người chăm sóc
- `services` - Dịch vụ chăm sóc
- `bookings` - Đặt dịch vụ
- `payments` - Thanh toán
- `reviews` - Đánh giá
- `support_tickets` - Tickets hỗ trợ
- `notifications` - Thông báo

## Testing với Postman/Thunder Client

1. Đăng ký/đăng nhập để lấy JWT token
2. Thêm token vào Authorization header cho các requests tiếp theo
3. Test các endpoints theo role tương ứng

## Lưu ý

- JWT secret key nên được thay đổi trong môi trường production
- Mật khẩu trong `data.sql` đã được mã hóa bằng BCrypt
- Cấu hình CORS có thể được tùy chỉnh trong `SecurityConfig.java`
- File upload chưa được implement, cần thêm logic để lưu file
- Payment gateway là mock, cần tích hợp với gateway thực tế

## Tác giả

Care Service Team - 2025

## License

This project is licensed under the MIT License.
