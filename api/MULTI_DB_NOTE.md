## Multi DB (2 connections) trong project này

### 1) Khai báo cấu hình trong `application.yml`

- **DB mặc định**: dùng Spring Boot auto-config theo `spring.datasource.*`
- **DB thứ 2**: project này khai báo theo `app.datasources.second.*`

Ví dụ (đã có sẵn trong file):

- `spring.datasource.url|username|password|driver-class-name` → DEFAULT
- `app.datasources.second.url|username|password|driver-class-name` → SECOND

### 2) Các file liên quan

- `api/src/main/java/com/example/backend/database/MultiDataSourceConfig.java`
  - Tạo bean `secondDataSource` từ prefix `app.datasources.second`
- `api/src/main/java/com/example/backend/database/ConnectionManager.java`
  - Quản lý 2 datasource: `DEFAULT` và `SECOND`
- `api/src/main/java/com/example/backend/database/DbContext.java`
  - Khi query/SP, `DbContext` sẽ lấy datasource theo `ConnectionManager.Connections`

### 3) Cách chọn DB khi chạy query/SP

Mặc định là `DEFAULT`.

Khi muốn chạy sang DB thứ 2:

```java
dbContext.setConnection(ConnectionManager.Connections.SECOND);
// gọi executeQuery/executeNonQuery/executeStoredProcedure... như bình thường
```

Muốn quay lại DB mặc định:

```java
dbContext.setConnection(ConnectionManager.Connections.DEFAULT);
```

### 4) Lưu ý quan trọng

- Nếu DB thứ 2 không cấu hình đúng (url/user/pass), ứng dụng sẽ fail khi khởi động vì bean `secondDataSource` không tạo được.
- Nếu bạn muốn “DB2 optional” (có thì dùng, không có vẫn chạy), mình có thể chỉnh lại `MultiDataSourceConfig` theo kiểu `@ConditionalOnProperty`.

