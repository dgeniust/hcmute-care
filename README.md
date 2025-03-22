# HCMUTE-care

Đây là dự án **HCMUTE-care**, bao gồm **backend** được xây dựng bằng Spring Boot và **frontend** được xây dựng bằng React. Mỗi phần có tệp `docker-compose.yml` và `.gitignore` riêng biệt để triển khai và quản lý độc lập.

## Tổng Quan

- **Backend**: Cung cấp API và xử lý logic nghiệp vụ, hỗ trợ nhiều cơ sở dữ liệu như Postgres hoặc MySQL.
- **Frontend**: Giao diện người dùng hiện đại, tương tác với backend qua API.
- **Containerization**: Sử dụng Docker và Docker Compose riêng cho từng phần.

---

## Prerequisite

Trước khi bắt đầu, hãy đảm bảo bạn đã cài đặt các công cụ sau:

### Công cụ chung

1. **Docker**
   - Kiểm tra phiên bản: `docker --version`
   - Nếu chưa cài, tải từ [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. **Docker Compose**
   - Kiểm tra phiên bản: `docker-compose --version`
   - Được cài kèm với Docker Desktop trên Windows/Mac.

### Công cụ cho Backend

1. **JDK 21+**
   - Kiểm tra phiên bản: `java -version`
   - Nếu chưa cài, tải từ [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) hoặc [OpenJDK](https://adoptium.net/).
2. **Maven 3.5+**
   - Kiểm tra phiên bản: `mvn -version`
   - Nếu chưa cài, tải từ [Apache Maven](https://maven.apache.org/download.cgi).
3. **IntelliJ IDEA** (khuyến nghị)
   - Tải từ [JetBrains IntelliJ](https://www.jetbrains.com/idea/download/).

### Công cụ cho Frontend

1. **Node.js 18+**
   - Kiểm tra phiên bản: `node -v`
   - Nếu chưa cài, tải từ [Node.js](https://nodejs.org/).
2. **npm** (đi kèm với Node.js)
   - Kiểm tra phiên bản: `npm -v`.
3. **Visual Studio Code** (khuyến nghị)
   - Tải từ [VS Code](https://code.visualstudio.com/).

> **Lưu ý**: Đảm bảo các công cụ được thêm vào biến môi trường PATH.

---

## Technical Stacks

### Backend

- **Java 21**: Ngôn ngữ lập trình chính.
- **Maven 3.5+**: Quản lý phụ thuộc và xây dựng dự án.
- **Spring Boot 3.4.4**: Framework chính.
- **Spring Data Validation**: Xác thực dữ liệu.
- **Spring Data JPA**: Thao tác cơ sở dữ liệu.
- **Postgres/MySQL (optional)**: Cơ sở dữ liệu (MySQL mặc định).
- **Lombok**: Giảm mã boilerplate.
- **DevTools**: Hỗ trợ phát triển nhanh.
- **Docker**: Đóng gói backend.

### Frontend

- **React 18**: Xây dựng giao diện người dùng.
- **Node.js 18+**: Môi trường chạy React.
- **npm**: Quản lý phụ thuộc.
- **Axios**: Gọi API từ frontend.
- **React Router**: Quản lý định tuyến.
- **Docker**: Đóng gói frontend.

---

## Cấu Trúc Thư Mục

```
hcmute-care/
├── /backend/ # Backend Spring Boot
│ ├── /src/ # Source code backend
│ ├── Dockerfile # Dockerfile cho backend
│ ├── docker-compose.yml # Docker Compose cho backend + database
│ ├── .gitignore # .gitignore riêng cho backend
| ├── README.md # Tài liệu riêng cho backend
│ └── pom.xml # Dependencies backend
├── /frontend/ # Frontend React
│ ├── /public/ # Tệp tĩnh
│ ├── /src/ # Source code frontend
│ ├── Dockerfile # Dockerfile cho frontend
│ ├── docker-compose.yml # Docker Compose cho frontend
│ ├── .gitignore # .gitignore riêng cho frontend
| ├── README.md # Tài liệu riêng cho backend
│ └── package.json # Dependencies frontend
└── README.md # Tài liệu này
```

## Cài Đặt

### 1. Sao chép dự án

Clone kho lưu trữ về máy:

```bash
git clone <URL-cua-repository>
cd hcmute-care
```

### 2. Chạy Backend với Docker Compose

```bash
cd backend
mvn clean package -P dev -DskipTests
docker-compose up --build
```

Backend chạy tại: http://localhost:8080.
Database (MySQL) chạy tại: localhost:3306.

### 3. Chạy Frontend với Docker Compose

```bash
cd frontend
docker-compose up --build
```

### 4. Chạy riêng Backend (không dùng Docker)

```bash
cd backend
mvn clean package -P dev -DskipTests
mvn spring-boot:run
```

### 5. Chạy riêng Frontend (không dùng Docker)

```bash
cd frontend
npm install
npm start
```
