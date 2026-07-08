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
let allSongs = []; // Nơi lưu trữ toàn bộ danh sách nhạc của hệ thống
let loopMode = 0; // 0: Off, 1: Loop 1, 2: Loop All, 3: Shuffle
let isShuffled = false; // Biến trạng thái
let originalQueue = []; // Lưu trữ thứ tự gốc khi chưa trộn

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
    loopMode = (loopMode + 1) % 3; // Cycle: 0 -> 1 -> 2 -> 0
    const tooltips = ["Không lặp", "Lặp 1 bài", "Lặp tất cả"];
    const loopBtn = document.getElementById('loopBtn');
    
    if (loopMode === 0) loopBtn.innerHTML = '<i class="fa-solid fa-times"></i>';      // Không lặp
    else if (loopMode === 1) loopBtn.innerHTML = '<i class="fa-solid fa-redo"></i>'; // Lặp 1 bài
    else if (loopMode === 2) loopBtn.innerHTML = '<i class="fa-solid fa-infinity"></i>';// Lặp tất cả

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

function toggleShuffle() {
    // 1. Đảo trạng thái
    isShuffled = !isShuffled;
    const shuffleBtn = document.getElementById('shuffleBtn');
    
    // 2. Xử lý Logic Trộn/Trả về
    if (isShuffled) {
        // Lưu queue gốc trước khi trộn
        originalQueue = [...queue]; 

        // Tách bài đang phát ra
        const currentSong = queue[currentIndex];
        let otherSongs = queue.filter((_, index) => index !== currentIndex);
        
        // Trộn các bài còn lại
        otherSongs.sort(() => Math.random() - 0.5);
        
        // Ghép lại: [Bài đang phát, ...Các bài đã trộn]
        queue = [currentSong, ...otherSongs];
        currentIndex = 0; // Bài đang phát luôn ở đầu
        
    } else {
        // Trả về gốc
        const currentPlayingUrl = queue[currentIndex].url;
        queue = [...originalQueue];
        // Tìm lại đúng vị trí của bài đang phát trong mảng gốc
        currentIndex = queue.findIndex(s => s.url === currentPlayingUrl);
    }

    // 3. Cập nhật UI nút bấm
    shuffleBtn.classList.toggle('btn-primary', isShuffled);
    shuffleBtn.classList.toggle('btn-outline-secondary', !isShuffled);

    // 4. BẮT BUỘC: Gọi hàm render danh sách ngay tại đây
    // Nếu bạn không gọi dòng này, danh sách sẽ không bao giờ update cho đến khi bạn load lại hoặc đổi bài
    const list = document.getElementById('queueDropdownList');
    
    // 1. Kiểm tra an toàn: nếu không tìm thấy phần tử DOM, thoát hàm
    if (!list) {
        console.error("Không tìm thấy phần tử có ID: queueDropdownList");
        return;
    }

    // 2. Làm sạch danh sách hiện tại
    list.innerHTML = ''; 

    // 3. Xử lý trường hợp danh sách rỗng
    if (queue.length === 0) {
        list.innerHTML = '<li class="p-2 text-muted text-center">Danh sách trống</li>';
        return;
    }

    // 4. Duyệt qua mảng queue đã trộn và render
    queue.forEach((song, index) => {
        const item = document.createElement('li');
        
        // Class CSS cho Bootstrap 5
        item.className = 'dropdown-item d-flex justify-content-between align-items-center py-2';
        item.style.cursor = 'pointer';
        
        // Nếu là bài đang phát, thêm class 'active' và icon nhạc
        if (index === currentIndex) {
            item.classList.add('active');
        }

        // Tạo nội dung HTML
        item.innerHTML = `
            <span class="text-truncate">${song.title}</span>
            ${index === currentIndex ? '<i class="fa-solid fa-music ms-2"></i>' : ''}
        `;
        
        // Gắn sự kiện click để phát bài hát
        item.onclick = () => {
            currentIndex = index; // Cập nhật chỉ số
            executePlay();        // Hàm phát nhạc của bạn
            
            // Tự động đóng dropdown sau khi chọn (UX Improvement)
            const dropdown = bootstrap.Dropdown.getInstance(document.querySelector('.dropdown-toggle'));
            if (dropdown) dropdown.hide();
        };

        list.appendChild(item);
    }); 
    
    console.log("Đã trộn xong và update UI!"); // Log để debug
}

// ==========================================
// 6. TIỆN ÍCH: Quản lý Queue & Âm lượng
// ==========================================
// Hàm thêm bài hát vào queue và phát ngay khi click vào tên bài hát
function playNow(url, title, artist) {
    queue = [{url, title, artist}]; // Reset queue và thêm bài mới
    currentIndex = 0;
    executePlay();
    if (typeof updateQueueMenu === 'function') updateQueueMenu();
}
// Hàm thêm bài hát vào queue nhưng không phát ngay (khi click vào nút "+ Thêm")
function addToQueue(url, title, artist) {
    if (!queue.some(song => song.url === url)) {
        queue.push({url, title, artist});
        if (currentIndex === -1) {
            currentIndex = queue.length - 1;
            executePlay();
        }
        if (typeof updateQueueMenu === 'function') updateQueueMenu();
        showToast("Đã thêm: " + title + " - " + artist);
    } else {
        showToast("Bài hát đã có trong hàng đợi!");
    }
}
/**
 * Thêm tất cả bài hát từ nguồn dữ liệu tổng vào queue
 *  'allSongs' chứa danh sách toàn bộ bài hát từ server
 */
// 3. Logic Add All (Thêm tất cả bài trên bảng)
function addAllToQueue() {
    // Lấy tất cả các dòng bài hát từ bảng (đã qua filter/search)
    const rows = document.querySelectorAll('#songTable .song-row');
    
    rows.forEach(row => {
        const url = row.cells[0].innerText
        const title = row.cells[1].innerText;
        const artist = row.cells[2].innerText;
        
        // Đẩy vào queue
        if(!queue.some(song => song.url === url)){
            queue.push({url: url, title: title, artist: artist });
        
            showToast("Đã thêm toàn bộ bài hát vào hàng đợi!");
            updateQueueDropdown();
        } else {
        showToast("Bài hát đã có trong hàng đợi!");
        }
    });
    
    
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

// Gọi hàm này khi trang web tải xong
document.addEventListener("DOMContentLoaded", () => {
    loadAllSongs();
    // ... các khởi tạo khác ...
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

function setVolume(val) { player.volume = val; }
function seekSong(val) { player.currentTime = (val / 100) * player.duration; }


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

// Tự động hóa: Khi bài hát kết thúc, gọi bộ điều phối
player.onended = () => executePlayNext();

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

