let queue = [];
let currentIndex = -1; // Trạng thái nghỉ
const player = document.getElementById('mainPlayer');
let currentParsedLyrics = [];

// 1. Luồng phát ngay: Xóa hàng đợi cũ, bắt đầu bài mới
function playNow(url, title) {
    queue = [{ url, title }];
    currentIndex = 0;
    executePlay();
    if (typeof updateQueueMenu === 'function') updateQueueMenu();
}

// 2. Luồng thêm vào hàng đợi
function addToQueue(url, title) {
    const isAlreadyInQueue = queue.some(song => song.url === url);

    if (!isAlreadyInQueue) {
        queue.push({ url, title });
        console.log("Đã thêm vào danh sách:", title);

        // Nếu trước đó đang ở trạng thái nghỉ, phát ngay bài này
        if (currentIndex === -1) {
            currentIndex = queue.length - 1;
            executePlay();
        }

        if (typeof updateQueueMenu === 'function') updateQueueMenu();
    } else {
        alert("Bài hát đã có trong danh sách phát.");
    }
}

// 3. Hàm thực thi phát  
function executePlay() {
    if (currentIndex >= 0 && currentIndex < queue.length) {
        player.src = queue[currentIndex].url;

        const nowPlaying = document.getElementById('nowPlaying');
        if (nowPlaying) nowPlaying.innerText = queue[currentIndex].title;

        player.load();

        // Xử lý an toàn với chính sách Autoplay của trình duyệt
        player.play().catch(e => {
            console.warn("Tương tác cần thiết để phát nhạc:", e);
        });

        const btn = document.getElementById('playPauseBtn');
        if (btn) btn.innerText = "⏸";
        updatePlayPauseButtonUI(true);

        // Xóa lời bài hát cũ khi chuyển sang bài mới để chuẩn bị nạp lời mới
        currentParsedLyrics = [];
        const container = document.getElementById('lyricContainer');
        if (container) container.innerHTML = `<span class="text-muted small italic">Chưa nạp lời. Hãy bấm "Xem lời" ở danh sách!</span>`;
    }
}

function nextSong() {
    if (queue.length === 0) return;

    // 1. Ưu tiên cao nhất: Lặp 1 bài
    if (loopMode === 1) {
        player.currentTime = 0;
        player.play();
        return;
    }

    // 2. Chế độ Xáo trộn
    if (loopMode === 3) {
        currentIndex = Math.floor(Math.random() * queue.length);
    }
    // 3. Chế độ Lặp tất cả (2) hoặc Bình thường (0)
    else {
        // Công thức quay vòng: (hiện tại + 1) chia lấy dư cho tổng số
        // Nếu ở bài cuối (n-1), (n-1+1) % n = 0 (quay về bài đầu)
        currentIndex = (currentIndex + 1) % queue.length;

        // Nếu ở chế độ Bình thường (0) mà đã quay về bài 0 (tức là đã hết danh sách)
        if (loopMode === 0 && currentIndex === 0) {
            player.pause();
            player.currentTime = 0;
            updatePlayPauseButtonUI(false); 
            return;
        }
    }

    executePlay();
}

function prevSong() {
    if (queue.length === 0) return;

    // Logic lùi quay vòng
    // (currentIndex - 1 + length) % length
    // Ví dụ: đang ở bài 0: (0 - 1 + 5) % 5 = 4 (bài cuối)
    currentIndex = (currentIndex - 1 + queue.length) % queue.length;

    executePlay();
}

// Hàm hỗ trợ UI (thêm vào nếu chưa có để đồng bộ nút bấm)
function updatePlayPauseButtonUI(isPlaying) {
    const btn = document.getElementById('playPauseBtn');
    if (btn) btn.innerText = isPlaying ? "⏸" : "⏯";
}
// Lắng nghe sự kiện kết thúc bài hát
player.onended = () => {
    if (typeof nextSong === 'function') {
        nextSong();
    }
};

// Cập nhật Duration khi metadata đã tải xong
player.onloadedmetadata = () => {
    const durationSpan = document.getElementById('duration');
    if (durationSpan) {
        durationSpan.innerText = formatTime(player.duration);
    }
};

function formatTime(seconds) {
    if (isNaN(seconds)) return "0:00";
    const m = Math.floor(seconds / 60);
    const s = Math.floor(seconds % 60);
    return `${m}:${s < 10 ? '0' : ''}${s}`;
}

// Trạng thái hiện tại: 0: Bình thường, 1: Lặp 1 bài, 2: Lặp tất cả, 3: Xáo trộn
let loopMode = 0;

function toggleLoopMode() {
    loopMode = (loopMode + 1) % 4;
    if (loopMode === 3) {
        shuffleQueue(); // Kích hoạt xáo trộn ngay khi chọn chế độ 3
    }
    updateLoopButtonUI();
}
function shuffleQueue() {
    // Fisher-Yates Shuffle
    for (let i = queue.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [queue[i], queue[j]] = [queue[j], queue[i]];
    }
    currentIndex = 0;
    executePlay();
}
function updateLoopButtonUI() {
    const btn = document.getElementById('loopBtn');
    const modes = [
        { icon: '⊘', text: 'Bình thường' },
        { icon: '🔂', text: 'Lặp 1 bài' },
        { icon: '🔁', text: 'Lặp tất cả' },
        { icon: '🔀', text: 'Xáo trộn' }
    ];
    btn.innerText = modes[loopMode].icon;
    btn.title = modes[loopMode].text;
   
    if (loopMode !== 0) {
        btn.classList.add('text-info');
    } else {
        btn.classList.remove('text-info');
    }
}

function toggleLyrics() {
    const panel = document.getElementById('lyricsPanel');
    panel.style.display = (panel.style.display === 'none') ? 'block' : 'none';
}

function openLyricsModal(title, content) {
    document.getElementById('lyricsTitle').innerText = title;
    document.getElementById('modalLyricsContent').innerText = content;

    // Sử dụng Bootstrap Modal API
    var myModal = new bootstrap.Modal(document.getElementById('lyricsModal'));
    myModal.show();
}
