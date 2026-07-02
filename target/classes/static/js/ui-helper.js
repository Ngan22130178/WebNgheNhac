/**
 * ui-helper.js - Bộ điều khiển giao diện người dùng
 * KIẾN TRÚC:
 * 1. Theme: Quản lý giao diện sáng/tối.
 * 2. Queue: Hiển thị và tương tác danh sách nhạc.
 * 3. Lyrics: Xử lý hiển thị và tải lời bài hát.
 * 4. Utilities: Các hàm hỗ trợ giao diện phụ.
 * 5. Initialization: Khởi tạo các sự kiện khi trang tải.
 */

// ==========================================
// 1. NHÓM QUẢN LÝ THEME (Sáng/Tối)
// ==========================================
function toggleTheme() {
    const body = document.body;
    const toggleBtn = document.getElementById('themeToggle');
    
    // Toggle các class nền và chữ
    body.classList.toggle('bg-dark');
    body.classList.toggle('text-white');
    
    // Kiểm tra trạng thái hiện tại
    const isDark = body.classList.contains('bg-dark');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    
    // Cập nhật icon
    if (toggleBtn) {
        toggleBtn.innerText = isDark ? '🌓' : '☀️';
    }
}

function initTheme() {
    const toggleBtn = document.getElementById('themeToggle');
    if (!toggleBtn) {
        console.error("Không tìm thấy nút themeToggle!");
        return;
    }

    // Gắn sự kiện click
    toggleBtn.onclick = toggleTheme; 
    
    // Áp dụng trạng thái ban đầu
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.add('bg-dark', 'text-white');
        toggleBtn.innerText = '🌓';
    } else {
        document.body.classList.remove('bg-dark', 'text-white');
        toggleBtn.innerText = '☀️';
    }
}

// ==========================================
// 2. NHÓM QUẢN LÝ DANH SÁCH PHÁT (Queue)
// ==========================================
function updateQueueMenu() {
    const list = document.getElementById('queueDropdownList');
    if (!list) return;
    list.innerHTML = ''; 

    if (queue.length === 0) {
        list.innerHTML = '<li class="p-2 text-muted text-center">Danh sách trống</li>';
        return;
    }

    queue.forEach((song, index) => {
        const item = document.createElement('li');
        item.className = 'dropdown-item d-flex justify-content-between align-items-center py-2';
        item.style.cursor = 'pointer';
        if (index === currentIndex) item.classList.add('active');

        item.innerHTML = `
            <span class="text-truncate">${song.title}</span>
            ${index === currentIndex ? '<small>🎵</small>' : ''}
        `;
        
        item.onclick = () => {
            currentIndex = index;
            executePlay(); 
            // Tự động đóng dropdown sau khi chọn bài (UX Improvement)
            const dropdown = bootstrap.Dropdown.getInstance(document.querySelector('.dropdown-toggle'));
            if (dropdown) dropdown.hide();
        };
        list.appendChild(item);
    });
}

// ==========================================
// 3. NHÓM QUẢN LÝ LỜI BÀI HÁT (Lyrics)
// ==========================================
async function loadLyricsMenu() {
    const menu = document.getElementById('lyricsMenu');
    const currentSong = queue[currentIndex];

    if (!menu) return;
    if (!currentSong) {
        menu.innerHTML = '<li class="px-3 text-muted">Chưa có bài hát nào</li>';
        return;
    }

    try {
        const response = await fetch(`/api/songs/${currentSong.id}/lyrics`);
        const lyricsList = await response.json();

        menu.innerHTML = '';
        if (lyricsList.length === 0) {
            menu.innerHTML = '<li class="px-3 text-muted">Không có lời bài hát</li>';
            return;
        }

        lyricsList.forEach(lyric => {
            const li = document.createElement('li');
            li.innerHTML = `<a class="dropdown-item" href="#">${lyric.language.toUpperCase()} (${lyric.format})</a>`;
            li.onclick = (e) => { e.preventDefault(); displayLyrics(lyric); };
            menu.appendChild(li);
        });
    } catch (e) {
        console.error("Lỗi khi load menu lời:", e);
    }
}

async function displayLyrics(lyric) {
    const modal = new bootstrap.Modal(document.getElementById('lyricsModal'));
    const contentDiv = document.getElementById('lyricsContent');
    const titleSpan = document.getElementById('lyricsTitle');
    
    titleSpan.innerText = `${queue[currentIndex].title} (${lyric.language})`;
    contentDiv.innerText = "Đang tải lời...";
    modal.show();

    try {
        if (lyric.fileUrl) {
            const res = await fetch(lyric.fileUrl);
            const text = await res.text();
            processAndShowLyrics(text, lyric.format);
        } else {
            processAndShowLyrics(lyric.content, lyric.format);
        }
    } catch (e) {
        contentDiv.innerText = "Không thể tải lời bài hát.";
    }
}

function processAndShowLyrics(text, format) {
    const contentDiv = document.getElementById('lyricsContent');
    if (format === 'LRC') {
        // Lọc sạch các tag thời gian và metadata của file LRC
        const cleaned = text.split('\n')
            .map(line => line.replace(/\[.*?\]/g, '').trim())
            .filter(line => line.length > 0)
            .join('\n');
        contentDiv.innerText = cleaned;
    } else {
        contentDiv.innerText = text;
    }
}

// ==========================================
// 4. TIỆN ÍCH HỖ TRỢ (Utilities)
// ==========================================
// (Dành cho các hàm xử lý phụ trợ khác nếu có)

// ==========================================
// 5. KHỞI TẠO SỰ KIỆN (Initialization)
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    // Các hàm khởi tạo listeners khác
});