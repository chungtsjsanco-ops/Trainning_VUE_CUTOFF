# Training Web Application

Ứng dụng web frontend mẫu dùng cho mục đích training. Được xây dựng bằng **Vue 2**, **Vuex**, **Vue Router**, **Axios** và **Bootstrap-Vue**.

---

## Yêu cầu hệ thống

| Công cụ | Phiên bản tối thiểu |
|---------|-------------------|
| Node.js | >= 6.0.0 |
| npm | >= 3.0.0 |

---

## Cấu trúc dự án

```
OneWeb/
├── build/                  # Webpack config
├── config/                 # Cấu hình môi trường (dev/prod)
├── src/
│   ├── api/                # Định nghĩa các hàm gọi API
│   ├── layouts/            # Layout chính (MainLayout, BlankLayout)
│   ├── modules/
│   │   ├── AUTH/Login/     # Trang đăng nhập
│   │   └── index.vue       # Trang chủ (Home)
│   ├── plugins/            # Axios, Auth, Token, Bootstrap-Vue, Toast
│   ├── router/             # Định nghĩa route
│   ├── store/              # Vuex store (module user)
│   └── utils/              # RequestContext (wrapper gọi API)
├── .env.example            # Mẫu cấu hình biến môi trường
└── index.html
```

---

## Hướng dẫn cài đặt & chạy

### Bước 1 — Chạy Backend trước

> ⚠️ **Bắt buộc** phải chạy backend trước. Frontend cần backend để thực hiện đăng nhập.

Backend cần expose API đăng nhập theo đúng chuẩn sau:

**`POST /api/auth/login`**

```json
// Request body
{
  "username": "admin",
  "password": "admin123"
}

// Response thành công (200)
{
  "error": "200",
  "error_code": "00000000",
  "message": "Thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "token_type": "Bearer",
    "username": "admin",
    "roles": ["ROLE_ADMIN"]
  },
  "request_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

Mặc định frontend sẽ gọi backend tại `http://localhost:8080`.

---

### Bước 2 — Cấu hình biến môi trường (tuỳ chọn)

Nếu backend chạy ở địa chỉ khác `localhost:8080`, tạo file `.env` tại thư mục gốc:

```bash
cp .env.example .env
```

Chỉnh sửa `.env`:

```env
VUE_APP_API=http://localhost:8080
```

> Nếu không tạo `.env`, mặc định sẽ dùng `http://localhost:8080`.

---

### Bước 3 — Cài đặt dependencies

```bash
npm install
```

---

### Bước 4 — Chạy môi trường development

```bash
npm run dev
```

Ứng dụng sẽ chạy tại: **http://localhost:8080** (hoặc port khác nếu đã bị chiếm)

Nếu muốn chạy với config dev thuần (không có testing mode):

```bash
npm run dev1
```

---

### Bước 5 — Build production

```bash
npm run build
```

Output sẽ nằm trong thư mục `dist/`.

---

## Luồng xác thực (Authentication Flow)

```
1. Người dùng truy cập bất kỳ route nào
       ↓
2. Router guard kiểm tra localStorage['app-token']
       ↓ không có token
3. Chuyển hướng đến /auth/login
       ↓ nhập username/password
4. Frontend gọi POST /api/auth/login
       ↓ thành công
5. Lưu token vào localStorage['app-token'] và Vuex store
       ↓
6. Chuyển hướng về trang Home
```

Token JWT được lưu với cấu trúc:

```json
{
  "token": "eyJhbGci...",
  "token_type": "Bearer",
  "username": "admin",
  "roles": ["ROLE_ADMIN"]
}
```

Mỗi request API sau đó sẽ tự động kèm header:

```
Authorization: Bearer eyJhbGci...
```

---

## Các thư viện chính

| Thư viện | Mục đích |
|----------|---------|
| `vue@2.6` | Framework frontend |
| `vuex` | Quản lý state (lưu thông tin user) |
| `vue-router` | Điều hướng SPA |
| `axios` + `vue-axios` | Gọi HTTP API |
| `bootstrap` + `bootstrap-vue` | UI components |
| `jwt-decode` | Decode JWT token |
| `vuex-persist` | Lưu Vuex state vào localStorage |
| `vue-toastification` | Hiển thị thông báo toast |
| `moment` | Xử lý ngày giờ |

---

## Trang demo sau khi đăng nhập

Sau khi đăng nhập thành công, trang Home hiển thị:

- **Tài khoản**: username lấy từ JWT token
- **Vai trò**: danh sách roles từ JWT token
- **Gọi API mẫu**: nút demo gọi `GET /api/sample` có kèm JWT token để kiểm tra luồng authenticated request

---

## Ghi chú khi phát triển thêm

- Thêm route mới: chỉnh sửa `src/router/index.js`
- Thêm API call: tạo file trong `src/api/` hoặc dùng trực tiếp `this.$root.context.get(url)` / `this.$root.context.post(url, data)`
- Thêm Vuex module: tạo file trong `src/store/` và đăng ký trong `src/store/index.js`
- Cấu hình base URL API: sửa biến `VUE_APP_API` trong `.env`
