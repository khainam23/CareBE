# Hướng dẫn Đăng ký - Care Service Booking System

## 🎯 Tổng quan

Hệ thống hỗ trợ 2 loại đăng ký:
1. **Khách hàng** - Người cần thuê dịch vụ chăm sóc
2. **Chuyên viên chăm sóc** - Người cung cấp dịch vụ

---

## 👥 1. Đăng ký Khách hàng

### Endpoint
```
POST http://localhost:8080/api/auth/register/customer
```

### Request Body
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

### Các trường thông tin

| Trường | Bắt buộc | Mô tả | Validation |
|--------|----------|-------|------------|
| email | ✅ | Email đăng nhập | Phải là email hợp lệ |
| password | ✅ | Mật khẩu | Tối thiểu 6 ký tự |
| fullName | ✅ | Họ và tên đầy đủ | 2-100 ký tự |
| phoneNumber | ✅ | Số điện thoại | 0xxxxxxxxx hoặc +84xxxxxxxxx |
| address | ❌ | Địa chỉ | - |
| emergencyContactName | ❌ | Tên người liên hệ khẩn cấp | - |
| emergencyContactPhone | ❌ | SĐT người liên hệ khẩn cấp | - |

### Response thành công
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

### Đặc điểm
- ✅ Tài khoản **ACTIVE** ngay lập tức
- ✅ Có thể đặt dịch vụ ngay sau khi đăng ký
- ✅ Nhận JWT token để sử dụng các API

### Ví dụ với cURL
```bash
curl -X POST http://localhost:8080/api/auth/register/customer \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "password123",
    "fullName": "Nguyễn Văn A",
    "phoneNumber": "0901234567",
    "address": "123 Đường ABC, Quận 1, TP.HCM"
  }'
```

---

## 🩺 2. Đăng ký Chuyên viên chăm sóc

### Endpoint
```
POST http://localhost:8080/api/auth/register/caregiver
```

### Request Body
```json
{
  "email": "caregiver@example.com",
  "password": "password123",
  "fullName": "Trần Thị C",
  "phoneNumber": "0912345678",
  "address": "456 Đường XYZ, Quận 3, TP.HCM",
  "bio": "Tôi có 5 năm kinh nghiệm chăm sóc người cao tuổi và người bệnh. Được đào tạo chuyên nghiệp về điều dưỡng và vật lý trị liệu.",
  "skills": "Chăm sóc người già, Điều dưỡng cơ bản, Vật lý trị liệu, Sơ cấp cứu",
  "yearsOfExperience": 5,
  "idCardNumber": "079123456789",
  "certifications": "Chứng chỉ Điều dưỡng viên cấp độ 1, Chứng chỉ Sơ cấp cứu quốc tế"
}
```

### Các trường thông tin

| Trường | Bắt buộc | Mô tả | Validation |
|--------|----------|-------|------------|
| email | ✅ | Email đăng nhập | Phải là email hợp lệ |
| password | ✅ | Mật khẩu | Tối thiểu 6 ký tự |
| fullName | ✅ | Họ và tên đầy đủ | 2-100 ký tự |
| phoneNumber | ✅ | Số điện thoại | 0xxxxxxxxx hoặc +84xxxxxxxxx |
| address | ✅ | Địa chỉ hiện tại | - |
| idCardNumber | ✅ | Số CMND/CCCD | 9-12 chữ số |
| bio | ❌ | Giới thiệu bản thân | - |
| skills | ❌ | Kỹ năng chuyên môn | - |
| yearsOfExperience | ❌ | Số năm kinh nghiệm | - |
| certifications | ❌ | Các chứng chỉ | - |

### Response thành công
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

### Đặc điểm
- ⏳ Tài khoản ở trạng thái **PENDING_APPROVAL**
- ⏳ Hồ sơ sẽ được Admin xem xét và phê duyệt
- ⏳ Chưa thể nhận việc cho đến khi được phê duyệt
- ✅ Nhận JWT token để theo dõi trạng thái hồ sơ

### Ví dụ với cURL
```bash
curl -X POST http://localhost:8080/api/auth/register/caregiver \
  -H "Content-Type: application/json" \
  -d '{
    "email": "caregiver@example.com",
    "password": "password123",
    "fullName": "Trần Thị C",
    "phoneNumber": "0912345678",
    "address": "456 Đường XYZ, Quận 3, TP.HCM",
    "bio": "5 năm kinh nghiệm chăm sóc người cao tuổi",
    "skills": "Chăm sóc người già, Điều dưỡng cơ bản",
    "yearsOfExperience": 5,
    "idCardNumber": "079123456789"
  }'
```

---

## 🔐 3. Đăng nhập

### Endpoint
```
POST http://localhost:8080/api/auth/login
```

### Request Body
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### Response
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "roles": ["ROLE_CUSTOMER"]
  }
}
```

---

## 🔄 4. Quy trình phê duyệt Chuyên viên

### Bước 1: Chuyên viên đăng ký
```
POST /api/auth/register/caregiver
```
- Trạng thái: `PENDING_APPROVAL`
- Verification: `PENDING`

### Bước 2: Admin xem xét hồ sơ
```
GET /api/admin/caregivers/pending
```
Admin xem danh sách Chuyên viên chờ duyệt

### Bước 3: Admin phê duyệt hoặc từ chối
```
POST /api/admin/caregivers/{id}/approve
hoặc
POST /api/admin/caregivers/{id}/reject
```

### Bước 4: Chuyên viên được kích hoạt
- Trạng thái: `ACTIVE`
- Verification: `APPROVED`
- Có thể nhận việc: `true`

---

## ❌ 5. Xử lý lỗi

### Lỗi email đã tồn tại
```json
{
  "success": false,
  "message": "Email đã được sử dụng!",
  "data": null
}
```

### Lỗi validation
```json
{
  "success": false,
  "message": "Email không hợp lệ",
  "data": null
}
```

### Lỗi thiếu thông tin bắt buộc
```json
{
  "success": false,
  "message": "Số điện thoại không được để trống",
  "data": null
}
```

---

## 📝 6. Best Practices

### Cho Khách hàng:
1. ✅ Cung cấp số điện thoại liên hệ khẩn cấp
2. ✅ Điền đầy đủ địa chỉ để Chuyên viên dễ tìm
3. ✅ Sử dụng mật khẩu mạnh (tối thiểu 8 ký tự)

### Cho Chuyên viên:
1. ✅ Viết bio chi tiết, chuyên nghiệp
2. ✅ Liệt kê đầy đủ kỹ năng và chứng chỉ
3. ✅ Cung cấp số CMND/CCCD chính xác
4. ✅ Số năm kinh nghiệm thực tế
5. ⚠️ Chờ Admin phê duyệt trước khi nhận việc

---

## 🧪 7. Testing với Postman

### Import Collection
1. Mở Postman
2. Import file `postman_collection.json`
3. Chọn folder "Authentication"
4. Test các endpoint:
   - Register Customer
   - Register Caregiver
   - Login

### Variables cần thiết
```
base_url = http://localhost:8080
token = (auto-set sau khi login)
```

---

## 📞 8. Hỗ trợ

Nếu gặp vấn đề trong quá trình đăng ký:
- 📧 Email: support@careservice.com
- 📱 Hotline: 1900-xxxx
- 💬 Live Chat: Trên website

---

## 🔗 9. Related APIs

Sau khi đăng ký thành công, bạn có thể sử dụng:

### Khách hàng:
- `GET /api/customers/me` - Xem profile
- `POST /api/bookings` - Đặt dịch vụ
- `GET /api/caregivers/search` - Tìm Chuyên viên

### Chuyên viên:
- `GET /api/caregivers/me` - Xem profile
- `PUT /api/caregivers/me` - Cập nhật profile
- `GET /api/caregivers/my-bookings` - Xem đơn hàng

Xem thêm trong [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
