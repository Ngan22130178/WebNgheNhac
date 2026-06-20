# 📘 Tài Liệu Thiết Kế Hệ Thống - Music App (V2.1 - Updated)

## 1. Tổng Quan Kỹ Thuật (Tech Stack)

* **Backend:** Java 21 (LTS), Spring Boot 4.0.6.
* **Database:** MongoDB (NoSQL) - Lưu trữ linh hoạt, hỗ trợ lời nhạc đa ngôn ngữ.
* **Frontend:**
  * **JSP + JSTL:** Template engine tạo giao diện server-side.
  * **HTMX:** Xử lý render động, giúp chuyển hướng trang không bị load lại (Single-Page feel).
  * **Bootstrap 5:** Giao diện Responsive & Dark Mode.
* **Định dạng lời nhạc:** `.lrc` (Parsed & Map-stored).

---

## 2. Mô Hình Dữ Liệu & Logic Trình Phát

### 2.1. Logic State Machine cho Player (Player-Core)

Hệ thống sử dụng một biến `loopMode` và danh sách `queue` làm nguồn sự thật duy nhất (Single Source of Truth).

| Trạng thái (`loopMode`) | Ý nghĩa | Hành vi `nextSong()` |
| :--- | :--- | :--- |
| `0` | Bình thường | Tăng index, dừng ở cuối queue. |
| `1` | Lặp 1 bài | Reset `currentTime = 0`, `play()` lại. |
| `2` | Lặp tất cả | Quay vòng về index 0 sau bài cuối. |
| `3` | Xáo trộn | Chọn index ngẫu nhiên (dùng Fisher-Yates). |

---

## 3. Quy trình xử lý lỗi Giao diện (HTMX Integration)

Để tránh lỗi **"Bảng lồng bảng"** và lặp lại Header/Navbar, hệ thống áp dụng quy chuẩn:

1. **Khung cố định (Layout):** `index.jsp` chứa `Navbar`, `Footer`, và `<table>` (khung `<thead>`).
2. **Khu vực thay đổi (Target):** `<tbody>` với `id="songListBody"`.
3. **Luồng HTMX:** Khi người dùng tương tác, HTMX chỉ fetch file JSP chứa các dòng `<tr>` và chèn vào `songListBody`.
* **Quy chuẩn:** Controller phải trả về view chỉ chứa `<c:forEach>` các hàng dữ liệu, không trả về `<html>` hoặc `navbar`.

---

## 4. Các Luồng Nghiệp Vụ Chính

### 4.1. Luồng Next/Prev (Vòng lặp danh sách)
Sử dụng toán tử Modulo (`%`) để đảm bảo tính tuần hoàn.

```mermaid
sequenceDiagram
    participant U as User
    participant CORE as Player-Core.js
    participant P as Audio Element

    U->>CORE: Nhấn Next / Prev
    CORE->>CORE: Tính toán currentIndex (Modulo %)
    Note over CORE: Xử lý LoopMode (0-3)
    CORE->>P: Cập nhật .src (bài hát mới)
    CORE->>P: .load() & .play()

```

---

## 5. Đặc Tả Implementation

### 5.1. Xử lý Lời nhạc (.lrc)

* **Regex:** `\[(\d{2}):(\d{2})\.(\d{2,3})\](.*)`
* **Lưu trữ:** Mỗi bài hát có một `lyricsMap` (key: mã ngôn ngữ, value: list các object `{time, content}`).

### 5.2. Tối ưu hóa Java 21

* **Virtual Threads:** Cấu hình `spring.threads.virtual.enabled=true` để tăng khả năng xử lý đồng thời cho các tác vụ I/O.

---

## 6. Danh mục Checklist Kiểm thử (QA Checklist)

* [ ] **Next/Prev:** Bài cuối bấm Next về đầu, bài đầu bấm Prev về cuối.
* [ ] **Loop Mode 1:** Phát lại chính bài đó, không chuyển bài.
* [ ] **HTMX Integration:** Không tải lại Header/Footer khi tìm kiếm.
* [ ] **Shuffle:** Thuật toán Fisher-Yates xáo trộn mảng `queue`.
* [ ] **Performance:** `Debounce` trên thanh tìm kiếm để tối ưu query DB.

---

## 7. Cấu Trúc Thư Mục

```text
MusicWeb-Project/
├── src/main/java/vn/edu/nlu/fit/musicweb/
│   ├── controller/      # Xử lý HTMX & API
│   ├── model/           # MongoDB entities
│   ├── repository/      # MongoDB Repositories
│   ├── service/         # Logic (Lyrics, Queue)
│   └── config/          # Cấu hình Virtual Threads
├── src/main/webapp/WEB-INF/jsp/
│   ├── index.jsp        # Layout chính
│   └── fragments/       # Các mảnh (songs_table, header, player)
└── src/main/resources/static/js/
    ├── player-core.js   # Logic Next/Prev/Loop/Shuffle
    └── ui-helper.js     # Utils, DOM updates

```

```

```
