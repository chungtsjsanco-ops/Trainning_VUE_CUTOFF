# Training Backend Project

Spring Boot REST API mẫu dùng cho training, gọi Oracle Stored Procedure qua JDBC, xác thực Basic Auth hoặc JWT Bearer Token.

---

## Kiến trúc tổng quan

```
Client (Postman / FE / Browser)
    │
    │  HTTP Request
    │  + Basic Auth header  HOẶC  Authorization: Bearer <token>
    ▼
┌─────────────────────────────────────────────┐
│            Spring Security Filter           │
│   JwtFilter (Bearer token)                  │
│   BasicAuthFilter (username/password)       │
└──────────────┬──────────────────────────────┘
               │ Authenticated
               ▼
┌─────────────────────────────────────────────┐
│         com.example.backend                 │
│                                             │
│  LoaiRackController  (/api/loai-rack)       │
│         │                                   │
│         ▼                                   │
│  LoaiRackService                            │
│         │                                   │
│         ▼                                   │
│  DbContext  (JDBC — gọi Oracle SP)          │
│         │                                   │
└─────────┼───────────────────────────────────┘
          │
          ▼
    Oracle Database
    (localhost:1521/ORCL)
```

---

## Cấu trúc source code

```
src/main/java/com/example/backend/
├── TrainingApplication.java          # Entry point
│
├── config/
│   ├── SecurityConfig.java           # Basic Auth + JWT filter chain
│   ├── SwaggerConfig.java            # Springdoc OpenAPI 3 config
│   ├── JwtUtil.java                  # Generate / validate JWT token
│   └── JwtFilter.java               # Filter đọc Bearer token mỗi request
│
├── auth/
│   ├── AuthController.java           # POST /api/auth/login
│   ├── LoginRequest.java             # DTO request login
│   └── LoginResponse.java            # DTO response (token, username, roles)
│
├── database/
│   ├── DbContext.java                # Thực thi Oracle SP/Function qua JDBC
│   ├── SqlParameter.java             # Wrapper tham số IN/OUT cho SP
│   ├── AppSqlException.java          # Exception từ Oracle (parse ORA-xxxxx)
│   ├── ConnectionManager.java        # Quản lý DataSource (single datasource)
│   └── SqlInjectionValidator.java    # Kiểm tra SQL injection cho string params
│
├── common/
│   ├── ApiResult.java                # Response wrapper chung cho tất cả API
│   ├── ResultOutput.java             # Wrapper cho kết quả đơn (Boolean/Object)
│   └── ModelValidation.java          # Hằng số thông báo validation
│
└── sample/
    ├── controller/
    │   └── LoaiRackController.java   # REST Controller mẫu
    ├── service/
    │   └── LoaiRackService.java      # Business logic mẫu
    └── dto/
        └── LoaiRackDTO.java          # Data Transfer Object mẫu

src/main/resources/
└── application.yml                   # Cấu hình ứng dụng
```

---

## Yêu cầu môi trường

| Công cụ | Phiên bản |
|---------|-----------|
| Java (JDK) | 8 trở lên |
| Maven | 3.6+ |
| Oracle Database | 11g / 12c / 19c |
| Oracle JDBC driver | ojdbc8 (đã khai báo trong pom.xml) |

---

## Cấu hình kết nối Oracle

Mở file `src/main/resources/application.yml` và sửa thông tin datasource:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/ORCL   # Thay host, port, SID/service name
    username: ecms                                 # Thay username Oracle
    password: ecms                                 # Thay password Oracle
    driver-class-name: oracle.jdbc.OracleDriver
```

> **Lưu ý:** Nếu dùng Service Name thay vì SID, đổi `@localhost:1521/ORCL` thành `@//localhost:1521/ORCL`.

---

## Chạy ứng dụng

### Cách 1 — Maven (command line)

```bash
# Đứng tại thư mục gốc của project (chứa pom.xml)
mvn spring-boot:run
```

### Cách 2 — Build JAR rồi chạy

```bash
# Build
mvn clean package -DskipTests

# Chạy
java -jar target/training-backend-0.0.1-SNAPSHOT.jar
```

### Cách 3 — Chạy thẳng trong IDE

Mở class `TrainingApplication.java`, nhấn **Run** (IntelliJ / Eclipse / VS Code với Extension Pack for Java).

---

Sau khi khởi động thành công, console sẽ hiển thị:

```
Started TrainingApplication in X.XXX seconds
Tomcat started on port(s): 8080
```

---

## Tài khoản đăng nhập

Ứng dụng có 2 tài khoản cố định (không cần DB), hỗ trợ **2 cách xác thực**:

| Username | Password | Role  |
|----------|----------|-------|
| `admin`  | `admin123` | ADMIN |
| `user`   | `user123`  | USER  |

**Cách 1 — Basic Auth** (dùng thẳng với Swagger, curl, Postman):
```
Authorization: Basic YWRtaW46YWRtaW4xMjM=
# hoặc truyền -u admin:admin123 trong curl
```

**Cách 2 — JWT Bearer Token** (khuyến nghị cho FE):
1. Gọi `POST /api/auth/login` để lấy token.
2. Gắn token vào header mỗi request: `Authorization: Bearer <token>`.

---

## Đăng nhập lấy JWT Token

### `POST /api/auth/login`

Endpoint **public** (không cần auth), trả về JWT token có hiệu lực **24 giờ**.

**Request:**

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response thành công (200):**

```json
{
  "error": "200",
  "error_code": "00000000",
  "message": "Thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6...",
    "token_type": "Bearer",
    "username": "admin",
    "roles": ["ROLE_ADMIN"]
  },
  "request_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response sai tài khoản (401):**

```json
{
  "error_code": "99999999",
  "message": "Tên đăng nhập hoặc mật khẩu không đúng"
}
```

**Dùng token cho các request tiếp theo:**

```http
GET http://localhost:8080/api/loai-rack/lay-danhsach
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Swagger UI — Test API trực tiếp trên trình duyệt

Truy cập sau khi khởi động:

```
http://localhost:8080/swagger-ui.html
```

1. Nhấn **Authorize** (biểu tượng khóa góc trên phải).
2. Nhập `admin` / `admin123` → **Authorize**.
3. Chọn API muốn test → **Try it out** → **Execute**.

---

## API Endpoints

### Base URL

```
http://localhost:8080
```

### `POST /api/loai-rack/lay-danhsach`

Lấy danh sách loại rack từ Oracle SP `ECMS.PKG_IDC_TAINGUYEN.SP_DANHSACH_LOAI_RACK`.

**Request:** không có body.

**Response thành công:**

```json
{
  "error": "200",
  "error_code": "00000000",
  "message": "Thành công",
  "message_detail": null,
  "data": [
    {
      "ID": 1,
      "TEN": "Rack 42U",
      "SO_U": 42
    }
  ],
  "request_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### `POST /api/loai-rack/capnhat`

Thêm mới hoặc cập nhật loại rack qua SP `ECMS.PKG_IDC_TAINGUYEN.SP_LOAI_RACK_UPDATE_INSERT`.

**Request body:**

```json
{
  "pId": 0,
  "pTen": "Rack 42U Standard",
  "pSoU": 42,
  "pDai": 600.0,
  "pRong": 800.0,
  "pCao": 2000.0,
  "pMaMau": "#000000",
  "pGhiChu": "Rack tiêu chuẩn",
  "pTenTat": "R42U",
  "pMoTa": "Rack 42U loại chuẩn",
  "pSuDung": 1
}
```

| Field | Bắt buộc | Mô tả |
|-------|----------|-------|
| `pId` | Không | `0` hoặc `null` = insert mới; giá trị > 0 = update |
| `pTen` | Có | Tên loại rack, tối đa 100 ký tự |
| `pSoU` | Có | Số U trên rack, số dương |
| `pDai` | Có | Chiều dài (mm), số dương |
| `pRong` | Có | Chiều rộng (mm), số dương |
| `pCao` | Có | Chiều cao (mm), số dương |
| `pSuDung` | Có | `1` = đang dùng, `0` = ngừng dùng |
| `pMaMau` | Không | Mã màu, tối đa 20 ký tự |
| `pGhiChu` | Không | Ghi chú, tối đa 500 ký tự |
| `pTenTat` | Không | Tên tắt, tối đa 20 ký tự |
| `pMoTa` | Không | Mô tả, tối đa 200 ký tự |

**Response thành công:**

```json
{
  "error": "200",
  "error_code": "00000000",
  "message": "Thành công",
  "data": { "result": true },
  "request_id": "..."
}
```

**Response lỗi (ví dụ validation):**

```json
{
  "error": "400",
  "error_code": "99999999",
  "message": "Lỗi xử lý",
  "message_detail": "ORA-20001: Tên loại rack đã tồn tại",
  "request_id": "..."
}
```

---

## Test bằng curl

### Dùng Basic Auth (đơn giản nhất)

```bash
# Lấy danh sách
curl -X POST http://localhost:8080/api/loai-rack/lay-danhsach \
  -u admin:admin123 \
  -H "Content-Type: application/json"

# Thêm mới
curl -X POST http://localhost:8080/api/loai-rack/capnhat \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "pId": 0,
    "pTen": "Rack 42U Standard",
    "pSoU": 42,
    "pDai": 600.0,
    "pRong": 800.0,
    "pCao": 2000.0,
    "pSuDung": 1
  }'
```

### Dùng JWT Token (cho FE)

```bash
# Bước 1 — Đăng nhập lấy token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'

# Bước 2 — Dùng token vừa nhận được (thay <TOKEN> bằng giá trị thực)
curl -X POST http://localhost:8080/api/loai-rack/lay-danhsach \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json"
```

---

## Hướng dẫn thêm API mới

Tạo thêm nghiệp vụ mới theo pattern của LoaiRack:

1. **DTO** — tạo `src/main/java/com/example/backend/sample/dto/MyEntityDTO.java`
2. **Service** — tạo `src/main/java/com/example/backend/sample/service/MyEntityService.java`, inject `DbContext`, gọi SP qua `dbContext.executeSpWithCursorToListMap(...)`
3. **Controller** — tạo `src/main/java/com/example/backend/sample/controller/MyEntityController.java`, annotate `@RestController`, `@RequestMapping("/api/my-entity")`

---

## Công nghệ sử dụng

| Thư viện | Phiên bản | Mục đích |
|----------|-----------|----------|
| Spring Boot | 2.4.5 | Framework chính |
| Spring Security | (managed) | Basic Auth + JWT Filter |
| jjwt | 0.11.5 | Tạo và xác thực JWT token |
| Spring Validation | (managed) | Validate request body |
| JDBC Core | 3.20.0 | Gọi Oracle SP/Function |
| Oracle JDBC (ojdbc8) | (managed) | Kết nối Oracle |
| Springdoc OpenAPI UI | 1.6.14 | Swagger UI |
