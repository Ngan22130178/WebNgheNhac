🛠️ Hướng Dẫn Từng Bước Cài Đặt Công Cụ & Khởi Tạo Dữ Liệu Cho Người Mới
Tài liệu này hướng dẫn chi tiết từ việc cài đặt ứng dụng gì, bấm vào nút nào, ở đâu để cấu hình Database thành công cho dự án.

PHẦN 1: Các phần mềm cần cài đặt vào máy tính
Bạn cần tải và cài đặt 2 công cụ cốt lõi sau để quản lý cơ sở dữ liệu:

MongoDB Community Server (Cơ sở dữ liệu chạy ngầm):

Tải ở đâu: Vào Google gõ cụm từ "Download MongoDB Community Server" (Chọn bản phù hợp với Windows hoặc macOS của bạn).

Cách cài: Bấm Next liên tục. Hãy tích chọn ô "Install MongoDB as a Service" trong quá trình cài đặt để nó tự khởi động cùng máy tính.

MongoDB Compass (Giao diện đồ họa để bấm chuột):

Tải ở đâu: Thường khi cài bản Server ở trên, nó sẽ hỏi có muốn cài kèm Compass không. Nếu không, bạn gõ Google "Download MongoDB Compass".

Tác dụng: Đây là ứng dụng có giao diện trực quan giúp bạn nhìn thấy data, thêm/sửa/xóa bằng chuột thay vì phải gõ lệnh đen ngòm.

PHẦN 2: Các bước thao tác trên giao diện MongoDB Compass
Sau khi cài đặt xong, bạn hãy mở ứng dụng MongoDB Compass lên và làm theo các bước sau:

Bước 1: Kết nối vào Database nội bộ (Local)
Khi vừa mở Compass, bạn sẽ thấy một ô chứa đường dẫn mặc định là: mongodb://localhost:27017.

Không cần sửa gì cả, bạn nhấn ngay nút Connect (Màu xanh lá cây).

Sau khi kết nối thành công, bạn sẽ nhìn thấy danh sách các database mặc định của hệ thống ở cột bên trái (như admin, config, local).

Bước 2: Tạo mới Database musicdb
Nhìn lên phía trên góc trái hoặc ở giữa màn hình, tìm và bấm vào nút "+" (Create database).

Một ô cửa sổ nhỏ hiện lên, bạn điền chính xác như sau:

Database Name: musicdb

Collection Name: songs (Bắt buộc phải gõ tên một collection đầu tiên để khởi tạo)

Nhấn nút Create Database. Lúc này ở cột bên trái sẽ xuất hiện chữ musicdb.

Bước 3: Tạo thêm các Collection (users, playlists)
Rê chuột vào tên musicdb ở cột bên trái, bạn sẽ thấy một dấu "+" xuất hiện ngay cạnh tên. Hãy bấm vào đó.

Ô cửa sổ hiện ra, tại mục Collection Name, điền: users rồi nhấn Create Collection.

Làm tương tự một lần nữa: Bấm dấu "+" cạnh musicdb, điền tên: playlists rồi nhấn Create Collection.

Kết quả: Hiện tại dưới thư mục musicdb, bạn đã có đủ 3 thư mục con (Collection) gồm: songs, users, và playlists.

PHẦN 3: Nạp dữ liệu mẫu (Import JSON)
Bây giờ chúng ta sẽ đưa dữ liệu chạy thử vào từng mục:

1. Nạp dữ liệu cho users
Ở cột bên trái, bấm chuột vào chữ users.

Ở vùng màn hình chính bên phải, tìm và bấm vào nút Add Data (Màu xanh dương) -> Chọn Insert document.

Một bảng nhập liệu hiện ra, mặc định sẽ có sẵn một dòng {"_id": ...}. Bạn hãy xóa sạch toàn bộ những chữ đang có trong ô đó đi.

Copy đoạn mã JSON dưới đây và dán (Paste) thẳng vào:

JSON
[
  {
    "_id": "660d1a2b9f1a2c3d4e5f6001",
    "username": "nguyenvanalover",
    "role": "ROLE_USER",
    "isEnabled": true
  },
  {
    "_id": "660d1a2b9f1a2c3d4e5f6002",
    "username": "admin_music",
    "role": "ROLE_ADMIN",
    "isEnabled": true
  }
]
Nhấn nút Insert ở góc dưới.

2. Nạp dữ liệu cho songs (Bài hát & Lời nhạc đa ngôn ngữ)
Bấm vào chữ songs ở cột bên trái.

Bấm nút Add Data -> Chọn Insert document -> Xóa sạch chữ cũ.

Copy toàn bộ đoạn code dưới đây và dán vào:

JSON
    [
      {
        "_id": "660d1b5c9f1a2c3d4e5f7001",
        "title": "Chúng Ta Của Tương Lai",
        "artist": "Sơn Tùng M-TP",
        "audioUrl": "https://res.cloudinary.com/demo/video/upload/v123456/chung_ta_cua_tuong_lai.mp3",
        "uploaderId": "660d1a2b9f1a2c3d4e5f6001",
        "lyricsMap": {
          "vi": [
            { "time": 0.0, "content": "[Đoạn dạo nhạc đầu bài]" },
            { "time": 12.5, "content": "Hình bóng ai đó nhẹ nhàng vụt qua nơi đây" },
            { "time": 16.8, "content": "Quyện vào làn gió thoảng hương dịu dàng đắm say" }
          ],
          "en": [
            { "time": 0.0, "content": "[Intro Music]" },
            { "time": 12.5, "content": "Someone's shadow gently passes by this place" },
            { "time": 16.8, "content": "Blending into the breeze with a sweet intoxicating scent" }
          ]
        }
      }
    ]
    ```
4.  Nhấn nút **Insert**.

### 3. Nạp dữ liệu cho `playlists`
1.  Bấm vào chữ `playlists` ở cột bên trái.
2.  Bấm nút **Add Data** -> Chọn **Insert document** -> Xóa sạch chữ cũ.
3.  Dán đoạn code này vào:
    
```json
    [
      {
        "_id": "660d1c7d9f1a2c3d4e5f8001",
        "name": "Nhạc Chill Cuối Tuần",
        "ownerId": "660d1a2b9f1a2c3d4e5f6001",
        "songIds": [
          "660d1b5c9f1a2c3d4e5f7001"
        ],
        "isPublic": true
      }
    ]
    ```
4.  Nhấn nút **Insert**.

---

## PHẦN 4: Cấu hình "Chỉ mục tìm kiếm" (Indexes) cho tên bài hát

Bước này giúp chức năng gõ từ khóa tìm kiếm gợi ý bài hát chạy siêu tốc.

1.  Tại cột bên trái, bấm vào mục `songs`.
2.  Ở màn hình chính bên phải, bạn nhìn lên thanh công cụ nằm ngang (ngay dưới tên collection), sẽ có các tab như: *Documents, Aggregations, Schema, **Indexes***. Bạn hãy bấm chọn tab **Indexes**.
3.  Nhìn sang bên phải, bấm vào nút **Create Index** (Màu xanh dương).
4.  Cửa sổ hiện lên cấu hình như sau:
    *   Ô **Configure the index keys**: Bạn gõ chữ `title` vào ô trống.
    *   Ô kế bên chọn kiểu (Type): Bấm mũi tên xổ xuống và chọn chữ **text** (Nếu không thấy chữ `text`, bạn có thể gõ số `1`).
5.  Nhấn nút **Review index** -> Nhấn tiếp **Create Index** để hoàn thành.

---

Đến đây, môi trường Database của bạn đã hoàn chỉnh 100%. Thành viên mới chỉ cần mở
