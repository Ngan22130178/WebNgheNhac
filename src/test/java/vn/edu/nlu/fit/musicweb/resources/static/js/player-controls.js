// src/main/resources/static/js/player-controls.js

function togglePlay() {
    const playPauseBtn = document.getElementById('playPauseBtn');
    if (player.paused) {
        player.play();
        playPauseBtn.innerText = "⏸";
    } else {
        player.pause();
        playPauseBtn.innerText = "⏯";
    }
}

function nextSong() {
    if (currentIndex < queue.length - 1) {
        currentIndex++;
        executePlay();
    }
}

function prevSong() {
    if (currentIndex > 0) {
        currentIndex--;
        executePlay();
    }
}

function setVolume(val) {
    player.volume = val;
}

// Hàm tua nhạc (được gọi bởi oninput trên thanh trượt)
function seekSong(val) {
    const time = (val / 100) * player.duration;
    player.currentTime = time;
}

// Cập nhật thanh trượt và thời gian hiển thị khi nhạc chạy
player.ontimeupdate = () => {
    const progressBar = document.getElementById('progressBar');
    const currentTimeSpan = document.getElementById('currentTime');
    
    if (progressBar) {
        const progress = (player.currentTime / player.duration) * 100;
        progressBar.value = progress || 0;
    }
    
    if (currentTimeSpan) {
        currentTimeSpan.innerText = formatTime(player.currentTime);
    }
};