# UteCare Backend

Đây là backend của ứng dụng UteCare, được xây dựng bằng Spring Boot và hỗ trợ nhiều cơ sở dữ liệu như Postgres hoặc MySQL. Dự án sử dụng Docker để triển khai dễ dàng trong môi trường phát triển.

## Prerequisite

Trước khi bắt đầu, hãy đảm bảo bạn đã cài đặt các công cụ sau:

1. **JDK 21+**
    - Kiểm tra phiên bản Java: `java -version`
    - Nếu chưa cài, tải từ [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) hoặc [OpenJDK](https://adoptium.net/).

2. **Maven 3.5+**
    - Kiểm tra phiên bản Maven: `mvn -version`
    - Nếu chưa cài, tải từ [Apache Maven](https://maven.apache.org/download.cgi).

3. **IntelliJ IDEA**
    - Nếu chưa cài, tải phiên bản Community hoặc Ultimate từ [JetBrains IntelliJ](https://www.jetbrains.com/idea/download/).

4. **Docker**
    - Kiểm tra phiên bản Docker: `docker --version`
    - Nếu chưa cài, tải từ [Docker Desktop](https://www.docker.com/products/docker-desktop/).

> **Lưu ý**: Đảm bảo các công cụ được thêm vào biến môi trường PATH để chạy từ terminal.

## Technical Stacks

Dự án sử dụng các công nghệ sau:

- **Java 21**: Ngôn ngữ lập trình chính.
- **Maven 3.5+**: Công cụ quản lý phụ thuộc và xây dựng dự án.
- **Spring Boot 3.4.4**: Framework chính cho backend.
- **Spring Data Validation**: Xác thực dữ liệu đầu vào.
- **Spring Data JPA**: Kết nối và thao tác với cơ sở dữ liệu.
- **Postgres/MySQL (optional)**: Tùy chọn cơ sở dữ liệu (MySQL được cấu hình mặc định).
- **Lombok**: Giảm mã boilerplate trong Java.
- **DevTools**: Hỗ trợ phát triển nhanh với Spring Boot.
- **Docker**: Đóng gói và triển khai ứng dụng.
- **Docker Compose**: Quản lý đa container (backend và database).

## Cài đặt

### 1. Sao chép dự án
Clone kho lưu trữ về máy:
```bash
git clone <URL-cua-repository>
cd <ten-thu-muc-du-an>

