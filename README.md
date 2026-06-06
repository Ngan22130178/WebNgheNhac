# 📘 Tài Liệu Thiết Kế Hệ Thống - Music App (V2.0)

## 1. Tổng Quan Kỹ Thuật (Tech Stack)

* **Backend:** Java 21 (LTS), Spring Boot 4.0.6.
* **Database:** MongoDB (NoSQL).
* **Kiến trúc:** Micro-monolith (hướng dịch vụ), AJAX-driven UI (không load lại trang để giữ nhạc).
* **Định dạng lời nhạc:** Standard `.lrc` (Lyric Runtime Control).

---

## 2. Mô Hình Dữ Liệu (Data Model)

Thiết kế tập trung vào tính linh hoạt của MongoDB để xử lý lời nhạc đa ngôn ngữ.

### 2.1. User Entity

Quản lý định danh và trạng thái tài khoản.

* `id`: String (ObjectId).
* `username`: String (Unique Index).
* `role`: Enum (ADMIN, USER).
* `isEnabled`: Boolean (Dùng cho chức năng khóa/mở tài khoản của Admin).

### 2.2. Song Entity (Multi-language Support)

Điểm mấu chốt là `lyricsMap` cho phép một bài hát có nhiều bản dịch.

```java
@Document(collection = "songs")
public class Song {
    @Id
    private String id;
    @TextIndexed
    private String title;  // Đánh chỉ mục Text để search gợi ý
    private String artist;
    private String audioUrl;
    private String uploaderId;
    
    // Key: "vi", "en", "jp"... 
    // Value: List các dòng lời tương ứng
    private Map<String, List<LyricLine>> lyricsMap; 
}

public class LyricLine {
    private double time;     // Thời điểm bắt đầu câu hát (giây)
    private String content;  // Nội dung câu hát
}

```

---

## 3. Các Luồng Nghiệp Vụ Chính (Sequence Diagrams)

### 3.1. Tìm kiếm không ngắt nhạc (AJAX Search)

Đảm bảo khi tìm kiếm, Player ở phía dưới không bị Reset.

```mermaid
sequenceDiagram
    participant U as User
    participant JS as AJAX Engine
    participant API as Spring Boot 4.0.6
    participant DB as MongoDB

    U->>JS: Nhập từ khóa (onInput)
    Note right of JS: Debounce 300ms tránh spam API
    JS->>API: GET /api/songs/search?q=...
    API->>DB: Query với @TextIndexed
    DB-->>API: Trả về kết quả
    API-->>JS: JSON (SongDTO)
    JS->>U: Hiển thị danh sách gợi ý

```

### 3.2. Đồng bộ lời nhạc & Chuyển bài (Lyric Sync)

Cơ chế xử lý lời nhạc thời gian thực tại Client.

```mermaid
sequenceDiagram
    participant A as Audio Element
    participant JS as Music Engine
    participant UI as Lyric Display (>> Mode)

    loop Mỗi 250ms
        A->>JS: Lấy currentTime
        JS->>JS: So khớp time với lyricsMap[lang]
        JS->>UI: Highlight dòng hiện tại & Smooth Scroll
    end

    A->>JS: Sự kiện 'ended'
    JS->>JS: Xử lý Logic (Shuffle / Repeat One - All)
    JS->>A: Cập nhật src bài mới & .play()

```

---

## 4. Đặc Tả Use Cases Cho Lập Trình Viên

### UC-01: Quản lý cá nhân (Self-Service)

* **Mô tả:** Người dùng quản lý Album, Playlist và thông tin cá nhân.
* **Lưu ý kỹ thuật:** Luôn kiểm tra `ownerId` tại tầng Service. Chỉ cho phép chỉnh sửa nếu `currentUser.id == resource.ownerId`.

### UC-02: Chế độ Xem lời (Lyric View Mode)

* **Giao diện:** Khi bấm nút `>>`, nội dung chính ẩn đi, màn hình lời toàn cảnh hiện lên.
* **Logic:** Chuyển đổi trạng thái bằng CSS Class thay vì load lại trang để bảo toàn luồng Audio.

### UC-03: Quản trị Admin

* **Mô tả:** Khóa người dùng, xóa bài hát vi phạm.
* **Bảo mật:** Sử dụng `@PreAuthorize("hasRole('ADMIN')")` tại Controller.

---

## 5. Hướng dẫn Triển Khai (Implementation Guide)

### 1. Xử lý File .lrc

Khi người dùng upload file `.lrc`, sử dụng Regex trong Java để parse.

* **Regex mẫu:** `\[(\d{2}):(\d{2})\.(\d{2,3})\](.*)`
* **Chuyển đổi:** `(phút * 60) + giây + (mili / 1000)` -> Lưu vào trường `time` (double).

### 2. Tối ưu Java 21

* Sử dụng **Virtual Threads** cho các tác vụ I/O nặng (như upload file lên Cloudinary) bằng cách cấu hình:
`spring.threads.virtual.enabled=true`.

### 3. Phân vùng UI

* **Fixed Player:** Luôn nằm ở Bottom.
* **Dynamic Content:** Nằm ở giữa, load qua AJAX.
* **Sidebar Queue:** Hiển thị danh sách bài tiếp theo khi click icon mở rộng.

---

## 6. Quy chuẩn Code (Coding Standard)

* **API:** Trả về định dạng JSON thống nhất qua `ResponseEntity<ResponseDTO>`.
* **Database:** Hạn chế xóa cứng (Hard Delete), ưu tiên dùng flag `isDeleted` hoặc `isEnabled`.
* **Frontend:** Sử dụng Vanilla JS hoặc các Lightweight Framework để đảm bảo tốc độ phản hồi.

# 📂 Cấu Trúc Thư Mục Dự Án & Chú Thích Ý Nghĩa Kỹ Thuật

```text
WebNgheNhac/
├── 📄 pom.xml                             # Nơi khai báo các thư viện (Maven Dependencies) như Spring Web, Mongo, Jasper.
├── 📄 mvnw.cmd                            # Bộ chạy Maven Wrapper trên Windows, giúp build dự án không cần cài cấu hình biến môi trường.
└── 📂 src/
    └── 📂 main/
        ├── 📂 java/vn/edu/nlu/fit/musicweb/
        │   │
        │   ├── 📄 MusicwebApplication.java      # Điểm khởi chạy (Entry Point) của toàn bộ ứng dụng Spring Boot.
        │   │
        │   ├── 📂 config/                       # [TẦNG CẤU HÌNH] - Thiết lập các thông số vận hành toàn cục của hệ thống.
        │   │   ├── 📄 MongoConfig.java          # Cấu hình kết nối DB, tự động quét và tạo các chỉ mục như Tìm kiếm văn bản.
        │   │   └── 📄 SecurityConfig.java       # Bộ lọc bảo mật: Chặn truy cập trái phép, phân quyền Admin, mã hóa mật khẩu User.
        │   │
        │   ├── 📂 model/                        # [TẦNG THỰC THỂ/ENTITY] - Định nghĩa cấu trúc dữ liệu lưu trong MongoDB.
        │   │   ├── 📄 User.java                 # Ánh xạ thông tin tài khoản người dùng, phân quyền (Role) và mảng bài hát yêu thích.
        │   │   ├── 📄 Song.java                 # Thực thể bài hát chính, chứa cấu trúc Map nhúng (lyricsMap) để lưu lời đa ngôn ngữ.
        │   │   └── 📄 LyricLine.java            # Cấu trúc con của câu hát, quy đổi mốc thời gian LRC về dạng số thực (giây) để máy tính hiểu.
        │   │
        │   ├── 📂 dto/                          # [DATA TRANSFER OBJECT] - Các lớp bọc dữ liệu trung gian khi truyền tải qua mạng.
        │   │   ├── 📄 SongDTO.java              # Bản rút gọn của Song (chỉ giữ tên, ca sĩ, ảnh). Dùng khi tìm kiếm để không phải tải mảng lời rất nặng.
        │   │   └── 📄 ResponseDTO.java          # Chuẩn hóa định dạng JSON đầu ra (status, message, data) giúp Frontend dễ xử lý lỗi.
        │   │
        │   ├── 📂 repository/                   # [TẦNG TRUY VẤN/DATA ACCESS] - Cung cấp các hàm giao tiếp trực tiếp với MongoDB.
        │   │   ├── 📄 UserRepository.java       # Chứa các hàm tìm kiếm tài khoản, phục vụ logic Đăng nhập/Đăng ký.
        │   │   └── 📄 SongRepository.java       # Chứa các hàm tìm kiếm bài hát theo từ khóa (@TextIndexed) hoặc bốc Top bài hát để làm BXH.
        │   │
        │   ├── 📂 service/                      # [TẦNG NGHIỆP VỤ/BUSINESS LOGIC] - Nơi xử lý các thuật toán và quy tắc cốt lõi của ứng dụng.
        │   │   ├── 📄 UserService.java          # Xử lý logic đăng ký (kiểm tra trùng tên), logic kiểm tra quyền sở hữu Playlist cá nhân.
        │   │   └── 📄 SongService.java          # Chứa thuật toán chạy Regex để dịch file text .lrc thành dữ liệu, xử lý bộ đếm lượt nghe.
        │   │
        │   └── 📂 controller/                   # [TẦNG TIẾP NHẬN REQUEST] - Cửa ngõ đón nhận các yêu cầu gửi lên từ giao diện người dùng.
        │       ├── 📄 PageController.java       # Dùng @Controller truyền thống để điều hướng và hiển thị các trang giao diện JSP.
        │       ├── 📄 SongController.java       # Dùng @RestController để trả về JSON, xử lý luồng Stream nhạc Byte-Range (tua nhạc).
        │       └── 📄 UserController.java       # Dùng @RestController để xử lý nhanh các tương tác như bấm nút Thả tim, thêm bài vào Queue.
        │
        ├── 📂 resources/                        # Thư mục chứa các tài nguyên bổ trợ ngầm cho Backend và các file tĩnh của Frontend.
        │   ├── 📂 static/                       # Nơi chứa tài nguyên tĩnh do trình duyệt tải về và lưu vào bộ nhớ đệm (Cache).
        │   │   ├── 📂 css/                      # Chứa file style.css để định hình giao diện (TailwindCSS) và các hiệu ứng chữ sáng lên khi chạy lời.
        │   │   ├── 📂 js/                       # Trọng tâm xử lý UX: audio-player.js (điều khiển nhạc, đồng bộ lời, lưu hàng đợi vào localStorage).
        │   │   └── 📂 assets/                   # Nơi chứa các tệp vật lý chạy thử của bạn (file nhạc .mp3, file lời mẫu .lrc, hình ảnh bìa).
        │   │
        │   └── 📄 application.properties        # File chứa các tham số cài đặt hệ thống (Cổng chạy, kết nối DB, bật luồng ảo Virtual Threads).
        │
        └── 📂 webapp/                           # Thư mục gốc chứa toàn bộ các trang giao diện hiển thị cho người dùng (Frontend).
            └── 📂 WEB-INF/
                └── 📂 views/                    # Vùng an toàn (Bảo mật cao), chặn người dùng truy cập trực tiếp file giao diện từ URL của trình duyệt.
                    ├── 📄 index.jsp             # Trang chủ cố định. Nơi chứa thanh điều khiển nhạc ở đáy trang (Fixed Bottom Player) không bao giờ reload.
                    │
                    └── 📂 fragments/            # [PHÂN VÙNG GIAO DIỆN ĐỘNG] - Nơi chứa các mảnh giao diện nhỏ để nạp vào giữa trang thông qua AJAX/HTMX.
                        ├── 📄 home-content.jsp  # Mảnh nội dung chứa danh sách bài hát mới, Bảng xếp hạng.
                        ├── 📄 search-results.jsp# Mảnh nội dung hiển thị danh sách kết quả sau khi người dùng tìm kiếm.
                        └── 📄 lyric-fullscreen.jsp # Mảnh giao diện kích hoạt Chế độ xem lời toàn màn hình khi người dùng bấm nút `>>`.


