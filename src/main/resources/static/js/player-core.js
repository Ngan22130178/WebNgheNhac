// ==========================================
// 2. CORE LOGIC: Phát nhạc
// ==========================================
function executePlay() {
    if (currentIndex >= 0 && currentIndex < queue.length) {
        // Cập nhật nguồn nhạc
        player.src = queue[currentIndex].url;
        
        // Cập nhật thông tin UI
        
        document.getElementById('nowPlaying').innerText = queue[currentIndex].title;
        document.getElementById('artistName').innerText = queue[currentIndex].artist || 'Unknown Artist';
        // CẬP NHẬT ẢNH BÌA: Nếu bạn có url ảnh trong object bài hát
        // Nếu không có, bạn có thể để ảnh mặc định hoặc bỏ qua
        if (queue[currentIndex].thumbnail) {
            document.getElementById('songThumb').src = queue[currentIndex].thumbnail;
        }

        player.load();
        player.play().catch(e => console.warn("Autoplay block:", e));
        
        updateMediaSession();

        updatePlayPauseUI(true);
        // Cập nhật lại màu sắc trong danh sách queue (đánh dấu bài đang phát)
        if (typeof updateQueueMenu === 'function') updateQueueMenu();
    }
}

function setVolume(val) { player.volume = val; }
function seekSong(val) { player.currentTime = (val / 100) * player.duration; }

// Tự động hóa: Khi bài hát kết thúc, gọi bộ điều phối
player.onended = () => executePlayNext();
