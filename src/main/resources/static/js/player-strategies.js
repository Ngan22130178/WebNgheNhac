// ==========================================
// 5. STRATEGY: Chiến lược phát theo Mode
// ==========================================

function updateMediaSession() {
    if ('mediaSession' in navigator && queue[currentIndex]) {
        const song = queue[currentIndex];
        
        // Cập nhật metadata hiển thị trên thanh thông báo/khóa màn hình
        navigator.mediaSession.metadata = new MediaMetadata({
            title: song.title,
            artist: song.artist || 'MusicWeb', // Nếu object bài hát của bạn có field artist
            artwork: song.thumbnail ? [{ src: song.thumbnail, sizes: '96x96', type: 'image/jpg' }] : []
        });

        // Đăng ký các sự kiện điều khiển
        navigator.mediaSession.setActionHandler('play', () => togglePlay());
        navigator.mediaSession.setActionHandler('pause', () => togglePlay());
        navigator.mediaSession.setActionHandler('previoustrack', () => prevSong());
        navigator.mediaSession.setActionHandler('nexttrack', () => nextSong());
        
        // Tùy chọn: Xử lý tua nhạc từ thông báo
        navigator.mediaSession.setActionHandler('seekbackward', (details) => {
            player.currentTime = Math.max(player.currentTime - (details.seekOffset || 10), 0);
        });
        navigator.mediaSession.setActionHandler('seekforward', (details) => {
            player.currentTime = Math.min(player.currentTime + (details.seekOffset || 10), player.duration);
        });
    }
}

// Hàm này gọi đến Controller của bạn để lấy danh sách nhạc
async function loadAllSongs() {
    try {
        const response = await fetch('/api/songs/all'); // Thay URL này bằng mapping Controller của bạn
        if (response.ok) {
            allSongs = await response.json(); // Gán dữ liệu vào biến toàn cục
            console.log("Đã tải xong toàn bộ danh sách nhạc:", allSongs);
        }
    } catch (error) {
        console.error("Lỗi khi tải danh sách nhạc:", error);
    }
}