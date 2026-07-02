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

// ==========================================
// 1. TRẠNG THÁI & BIẾN TOÀN CỤC
// ==========================================
let queue = [];
let currentIndex = -1;
let loopMode = 0; // 0: Off, 1: Loop 1, 2: Loop All, 3: Shuffle
const player = document.getElementById('mainPlayer');

// Khởi tạo trạng thái giao diện khi trang vừa tải xong
document.addEventListener("DOMContentLoaded", () => {
    // Đảm bảo nút Loop hiển thị đúng mode 0 (⊘)
    const loopBtn = document.getElementById('loopBtn');
    if (loopBtn) {
        loopBtn.innerText = '⊘';
    }
});

/**
 * Giả lập Collections.shuffle của Java
 * @param {Array} array - Danh sách cần xáo trộn
 */
function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}

// ==========================================
// 2. CORE LOGIC: Phát nhạc
// ==========================================
function executePlay() {
    if (currentIndex >= 0 && currentIndex < queue.length) {
        // Cập nhật nguồn nhạc
        player.src = queue[currentIndex].url;
        
        // Cập nhật thông tin UI
        document.getElementById('nowPlaying').innerText = queue[currentIndex].title;
        
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

// Hàm cập nhật menu lời bài hát
function updateQueueMenu() {
    const list = document.getElementById('queueDropdownList');
    if (!list) return;
    list.innerHTML = ''; 

    queue.forEach((song, index) => {
        const item = document.createElement('div');
        // Thêm class 'd-flex justify-content-between align-items-center' để dàn hàng ngang
        item.className = `p-2 border-bottom d-flex justify-content-between align-items-center ${index === currentIndex ? 'bg-primary text-white' : ''}`;
        
        // Tạo HTML cho tên bài hát và hiệu ứng sóng nhạc
        let equalizer = '';
        if (index === currentIndex) {
            equalizer = `
                <div class="equalizer">
                    <span></span><span></span><span></span>
                </div>`;
        }

        item.innerHTML = `<span>${song.title}</span> ${equalizer}`;
        item.style.cursor = 'pointer';
        
        item.onclick = () => {
            currentIndex = index;
            executePlay();
        };
        list.appendChild(item);
    });
}

// ==========================================
// 3. ĐIỀU KHIỂN: Nút bấm (Luôn tuần tự)
// ==========================================
// Hàm cập nhật menu queue (đánh dấu bài đang phát)
function togglePlay() {
    if (player.paused) { player.play(); updatePlayPauseUI(true); }
    else { player.pause(); updatePlayPauseUI(false); }
}
// Nút bấm NEXT
function nextSong() {
    if (queue.length === 0) return;
    // Nút bấm: Luôn chuyển tới bài tiếp theo tuần tự
    currentIndex = (currentIndex + 1) % queue.length;
    executePlay();
}

// Nút bấm PREV
function prevSong() {
    if (queue.length === 0) return;
    // Nút bấm: Luôn chuyển về bài trước tuần tự
    currentIndex = (currentIndex - 1 + queue.length) % queue.length;
    executePlay();
}
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

// ==========================================
// 4. DISPATCHER: Điều phối tự động (Khi hết bài)
// ==========================================
// Hàm này sẽ được gọi khi bài hát kết thúc (player.onended)
function executePlayNext() {
    switch (loopMode) {
        case 0: handleLoopOff(); break;
        case 1: handleLoopOne(); break;
        case 2: handleLoopAll(); break;
        case 3: handleShuffle(); break;
        default: handleLoopAll(); break;
    }
}

// ==========================================
// 5. STRATEGY: Chiến lược phát theo Mode
// ==========================================
// Mode 0: Không lặp, chuyển sang bài tiếp theo, nếu hết thì dừng
function toggleLoopMode() {
    // 1. Chuyển đổi mode (0 -> 1 -> 2 -> 3 -> 0)
    loopMode = (loopMode + 1) % 4;
    
    // 2. Cập nhật giao diện nút bấm
    const btn = document.getElementById('loopBtn');
    const modes = ['⊘', '🔂', '🔁', '🔀'];
    const tooltips = ['Không lặp', 'Lặp 1 bài', 'Lặp tất cả', 'Phát ngẫu nhiên'];
    
    if (btn) {
        btn.innerText = modes[loopMode];
        btn.title = tooltips[loopMode];
    }
    
    // 3. Thông báo cho người dùng
    showToast("Chế độ: " + tooltips[loopMode]);
}
// Mode 0: Không lặp, chuyển sang bài tiếp theo, nếu hết thì dừng
function handleLoopOff() {
    if (currentIndex < queue.length - 1) {
        currentIndex++;
        executePlay();
    } else {
        player.pause();
        updatePlayPauseUI(false);
    }
}
// Mode 1: Lặp lại bài hiện tại
function handleLoopOne() {
    player.currentTime = 0;
    player.play();
}
//  Mode 2: Phát lại toàn bộ queue khi hết bài
function handleLoopAll() {
    currentIndex = (currentIndex + 1) % queue.length;
    executePlay();
}

// Mode 3: Phát ngẫu nhiên, nhưng không lặp lại bài đang phát nếu có nhiều hơn 1 bài
function handleShuffle() {
    if (queue.length <= 1) return;

    shuffle(queue);

    // Chọn ngẫu nhiên một index thay vì mặc định là 0
    currentIndex = Math.floor(Math.random() * queue.length);
    
    if (typeof updateQueueMenu === 'function') updateQueueMenu();
    executePlay();
    
    showToast("Đã xáo trộn danh sách!");
}

// ==========================================
// 6. TIỆN ÍCH: Quản lý Queue & Âm lượng
// ==========================================
// Hàm thêm bài hát vào queue và phát ngay khi click vào tên bài hát
function playNow(url, title) {
    queue = [{url, title}];
    currentIndex = 0;
    executePlay();
    if (typeof updateQueueMenu === 'function') updateQueueMenu();
}
// Hàm thêm bài hát vào queue nhưng không phát ngay (khi click vào nút "+ Thêm")
function addToQueue(url, title) {
    if (!queue.some(song => song.url === url)) {
        queue.push({url, title});
        if (currentIndex === -1) {
            currentIndex = queue.length - 1;
            executePlay();
        }
        if (typeof updateQueueMenu === 'function') updateQueueMenu();
        showToast("Đã thêm: " + title);
    } else {
        showToast("Bài hát đã có trong hàng đợi!");
    }
}
// Hàm hiển thị thông báo (Toast) cho người dùng không cần bấm xác nhận, tự động ẩn sau vài giây
function showToast(message) {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toastMessage');
    toastBody.innerText = message;
    new bootstrap.Toast(toastEl).show();
}

function setVolume(val) { player.volume = val; }
function seekSong(val) { player.currentTime = (val / 100) * player.duration; }


// ==========================================
// 7. UI SYNC & EVENTS
// ==========================================
// Hàm cập nhật trạng thái nút Play/Pause
function updatePlayPauseUI(isPlaying) {
    document.getElementById('playPauseBtn').innerText = isPlaying ? "⏸" : "⏯";
}

player.ontimeupdate = () => {
    const progressBar = document.getElementById('progressBar');
    const currentTimeSpan = document.getElementById('currentTime'); // Số bên trái
    const durationSpan = document.getElementById('duration');       // Số bên phải

    if (player.duration) {
        // 1. Cập nhật thanh tiến trình
        progressBar.value = (player.currentTime / player.duration) * 100;
        
        // 2. Cập nhật thời gian đã phát (bên trái)
        currentTimeSpan.innerText = formatTime(player.currentTime);
        
        // 3. Cập nhật thời gian còn lại (bên phải)
        const timeLeft = player.duration - player.currentTime;
        durationSpan.innerText = "-"+ formatTime(timeLeft);
    }
};
// Hàm định dạng thời gian (phút:giây)
function formatTime(s) {
    const m = Math.floor(s / 60);
    const sec = Math.floor(s % 60);
    return `${m}:${sec < 10 ? '0' : ''}${sec}`;
}

// Tự động hóa: Khi bài hát kết thúc, gọi bộ điều phối
player.onended = () => executePlayNext();

// ==========================================
// CẬP NHẬT MEDIA SESSION API
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
