/**
 * player.js - Quản lý toàn bộ chức năng trình phát nhạc
 * * KIẾN TRÚC HỆ THỐNG:
 * 1. State (Trạng thái): Quản lý queue, currentIndex, loopMode.
 * 2. Core Logic (Thực thi): executePlay() cập nhật media và UI.
 * 3. Controls (Điều khiển): Nút bấm (nextSong, prevSong) thực hiện hành động tuần tự.
 * 4. Dispatcher (Điều phối): executePlayNext() nhận yêu cầu tự động, phân loại theo mode.
 * 5. Strategy (Chiến lược): Các hàm handleLoop xử lý logic tính toán index khi hết bài.
 * 6. UI & Events: Đồng bộ giao diện và tự động hóa qua player.onended.
 */



