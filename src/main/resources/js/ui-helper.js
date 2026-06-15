// src/main/resources/static/js/ui-helper.js

function toggleTheme() {
    const body = document.body;
    body.classList.toggle('bg-dark');
    body.classList.toggle('text-white');
    const isDark = body.classList.contains('bg-dark');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    
    // Cập nhật icon ngay khi click
    const toggleBtn = document.getElementById('themeToggle');
    if (toggleBtn) toggleBtn.innerText = isDark ? '🌓' : '☀️';
}

document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.getElementById('themeToggle');
    if (toggleBtn) {
        // Gán sự kiện click vào nút
        toggleBtn.addEventListener('click', toggleTheme);
        
        // Khởi tạo trạng thái ban đầu
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme === 'light') {
            document.body.classList.remove('bg-dark');
            document.body.classList.remove('text-white');
            toggleBtn.innerText = '☀️';
        } else {
            document.body.classList.add('bg-dark');
            document.body.classList.add('text-white');
            toggleBtn.innerText = '🌓';
        }
    }
});
function updateQueueMenu() {
    const list = document.getElementById('queueDropdownList');
    list.innerHTML = ''; // Xóa nội dung cũ

    if (queue.length === 0) {
        list.innerHTML = '<li class="p-2 text-muted text-center">Danh sách trống</li>';
        return;
    }

    queue.forEach((song, index) => {
        const item = document.createElement('li');
        item.className = 'dropdown-item d-flex justify-content-between align-items-center py-2';
        item.style.cursor = 'pointer';
        if (index === currentIndex) item.classList.add('active'); // Đánh dấu bài đang phát

        item.innerHTML = `
            <span class="text-truncate">${song.title}</span>
            ${index === currentIndex ? '<small>🎵</small>' : ''}
        `;
        
        item.onclick = () => {
            currentIndex = index;
            executePlay(); // Phát bài đã chọn
            updateQueueMenu(); // Cập nhật lại giao diện menu
        };
        list.appendChild(item);
    });
}

async function loadLyricsMenu() {
    const menu = document.getElementById('lyricsMenu');
    const currentSong = queue[currentIndex]; // Lấy bài đang phát từ queue

    if  (!menu) return;
    if (!currentSong) {
        menu.innerHTML = '<li class="px-3 text-muted">Chưa có bài hát nào</li>';
        return;
    }

    // Gọi API lấy danh sách lời của bài hát này
    try {
        const response = await fetch(`/api/songs/${currentSong.id}/lyrics`);
        const lyricsList = await response.json();

        menu.innerHTML = ''; // Xóa menu cũ
        if (lyricsList.length === 0) {
            menu.innerHTML = '<li class="px-3 text-muted">Không có lời bài hát</li>';
            return;
        }

        lyricsList.forEach(lyric => {
            const li = document.createElement('li');
            li.innerHTML = `<a class="dropdown-item" href="#">${lyric.language.toUpperCase()} (${lyric.format})</a>`;
            li.onclick = () => displayLyrics(lyric); // Chuyển sang Bước C
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

    // Nếu có fileUrl thì tải file, nếu không thì lấy content trực tiếp
    if (lyric.fileUrl) {
        const res = await fetch(lyric.fileUrl);
        const text = await res.text();
        processAndShowLyrics(text, lyric.format);
    } else {
        processAndShowLyrics(lyric.content, lyric.format);
    }
}

function processAndShowLyrics(text, format) {
    const contentDiv = document.getElementById('lyricsContent');
    if (format === 'LRC') {
        // Xóa timestamp [mm:ss.xx] để hiển thị
        contentDiv.innerText = text.replace(/\[\d{2}:\d{2}(\.\d{2})?\]/g, '').trim();
    } else {
        contentDiv.innerText = text;
    }
}