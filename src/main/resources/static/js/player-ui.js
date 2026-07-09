// ==========================================
// 7. UI SYNC & EVENTS
// ==========================================
// Hàm cập nhật trạng thái nút Play/Pause
function updatePlayPauseUI(isPlaying) {
    const playBtn = document.getElementById('playPauseBtn');
    const icon = playBtn.querySelector('i'); // Lấy thẻ <i> bên trong nút

    if (isPlaying) {
        // Đang phát -> đổi sang icon Tạm dừng
        icon.className = "fa-solid fa-pause";
        playBtn.title = "Tạm dừng";
        playBtn.classList.add('btn-primary'); // Nút sáng lên khi đang phát
        playBtn.classList.remove('btn-outline-secondary');
    } else {
        // Đang dừng -> đổi sang icon Phát
        icon.className = "fa-solid fa-play";
        playBtn.title = "Phát";
        playBtn.classList.add('btn-outline-secondary');
        playBtn.classList.remove('btn-primary');
    }
}

player.ontimeupdate = () => {
    const progressBar = document.getElementById('progressBar');
    const currentTimeSpan = document.getElementById('currentTime'); 
    const durationSpan = document.getElementById('duration');       

    if (player.duration) {
        // Tính toán phần trăm hoàn thành
        const percent = (player.currentTime / player.duration) * 100;
        
        // 1. Cập nhật giá trị thanh trượt
        progressBar.value = percent;
        
        // 2. CẬP NHẬT BIẾN CSS ĐỂ TÔ MÀU (Quan trọng)
        progressBar.style.setProperty('--progress', percent + '%');
        
        // 3. Cập nhật thời gian đã phát (bên trái)
        currentTimeSpan.innerText = formatTime(player.currentTime);
        
        // 4. Cập nhật thời gian còn lại (bên phải)
        const timeLeft = player.duration - player.currentTime;
        durationSpan.innerText = "-" + formatTime(timeLeft);
    }
};
// Hàm định dạng thời gian (phút:giây)
function formatTime(s) {
    const m = Math.floor(s / 60);
    const sec = Math.floor(s % 60);
    return `${m}:${sec < 10 ? '0' : ''}${sec}`;
}

// Gọi hàm này khi trang web tải xong
document.addEventListener("DOMContentLoaded", () => {
    loadAllSongs();
   
});

// Khởi tạo trạng thái giao diện khi trang vừa tải xong
document.addEventListener("DOMContentLoaded", () => {
    // Đảm bảo nút Loop hiển thị đúng mode 0 (⊘)
    const loopBtn = document.getElementById('loopBtn');
    if (loopBtn) {
        loopBtn.innerText = '⊘';
    }
});

// Thêm phím tắt bàn phím cho Play/Pause, Next, Prev
document.addEventListener('keydown', (e) => {
    if (e.code === 'Space') { // Space để Play/Pause
        e.preventDefault();
        togglePlay();
    } else if (e.code === 'ArrowRight') { // Next
        nextSong();
    } else if (e.code === 'ArrowLeft') { // Prev
        prevSong();
    }
});

// Hàm hiển thị thông báo (Toast) cho người dùng không cần bấm xác nhận, tự động ẩn sau vài giây
function showToast(message) {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toastMessage');
    toastBody.innerText = message;
    // Khởi tạo và hiển thị toast
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}



// Hàm Download bài hát hiện tại
function downloadCurrentSong() {
    // Lấy URL từ nguồn phát hiện tại
    const currentUrl = player.src; 
    
    if (currentUrl) {
        // Tạo một thẻ a ẩn để kích hoạt tải xuống
        const a = document.createElement('a');
        a.href = currentUrl;
        a.download = document.getElementById('nowPlaying').innerText || 'song.mp3';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    } else {
        showToast("Không tìm thấy tệp nhạc để tải!", "#dc3545");
    }
}

/// player-ui.js
function toggleLyrics() {
    // 1. Lấy bài hát hiện tại từ hàng đợi (queue)
    // Queue và currentIndex là biến toàn cục từ player-state.js
    const currentSong = queue[currentIndex];

    // 2. Kiểm tra dữ liệu
    if (!currentSong || !currentSong.id) {
        showToast("Vui lòng phát một bài hát trước!", "#ffc107");
        return;
    }

    const lyricsContainer = document.getElementById('lyrics-container');
    
    // 3. Toggle hiển thị
    if (lyricsContainer.style.display === 'block') {
        lyricsContainer.style.display = 'none';
    } else {
        lyricsContainer.style.display = 'block';
        loadLyricsForSong(currentSong.id); // Dùng ID chuẩn từ object
    }
}

// Hàm tải nội dung lời từ server
function loadLyricsForSong(songId) {
    const lyricsContent = document.getElementById('lyrics-content');
    lyricsContent.innerText = "Đang tải lời bài hát...";

    fetch(`/api/lyrics/song/${songId}`)
        .then(response => {
            if (!response.ok) throw new Error("Không tìm thấy lời!");
            return response.json();
        })
        .then(data => {
            lyricsContent.innerText = data.content;
        })
        .catch(err => {
            lyricsContent.innerText = "Lời bài hát hiện chưa có.";
            console.error(err);
        });
}
