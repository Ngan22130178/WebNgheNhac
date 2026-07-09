/**
 * lyrics-handler.js - Xử lý lời bài hát nâng cao (LRC/TXT)
 */

let lyricsData = []; // Lưu trữ mảng { time, text }
/**
 * Tải và phân tích lời bài hát
 * @param {string|number} songId - ID của bài hát
 * @param {string|null} lang - Mã ngôn ngữ (ví dụ: 'vi', 'en') hoặc null để lấy mặc định
 */
async function loadAndParseLyrics(songId, lang = null) {
    const container = document.getElementById(`lyric-text-box-${songId}`);
    const row = document.getElementById(`lyric-row-${songId}`);
    
    if (!container || !row) return;

    // Reset dữ liệu cũ để tránh lỗi sync giữa các bài
    lyricsData = [];
    container.innerHTML = '<em><i class="fa-solid fa-spinner fa-spin"></i> Đang tải...</em>';
    row.style.display = "table-row";

    try {
        // 1. Tạo URL động: Nếu có lang thì gắn vào, không thì gọi mặc định
        const url = lang ? `/api/lyrics/${songId}?lang=${lang}` : `/api/lyrics/${songId}`;
        const res = await fetch(url);
        
        if (!res.ok) throw new Error("Không lấy được dữ liệu lời");
        
        const text = await res.text();
        
        // 2. Phân tích nội dung (Kiểm tra format LRC qua regex tag thời gian)
        // Regex kiểm tra xem có dòng nào chứa định dạng [mm:ss.xx] không
        const isLRC = /\[\d{2}:\d{2}(\.\d{2})?\]/.test(text);
        
        if (isLRC) {
            lyricsData = parseLRC(text);
        } else {
            // Nếu là TXT, tạo mảng 1 phần tử với time = 0
            lyricsData = [{ time: 0, text: text }];
        }
        
        // 3. Render giao diện
        displayLyrics(container);
        
        // 4. Nếu có nhiều hơn 1 dòng và là LRC (có thời gian), mới khởi động sync
        if (lyricsData.length > 0 && lyricsData.some(line => line.time > 0)) {
            startLyricSync();
        }
        
    } catch (e) {
        console.error("Lỗi khi tải lời:", e);
        container.innerHTML = '<em class="text-danger">Hiện chưa có lời cho bài hát này.</em>';
    }
}

// Tách thời gian và nội dung
function parseLRC(lrcText) {
    const lines = lrcText.split('\n');
    return lines.map(line => {
        const match = line.match(/\[(\d{2}):(\d{2}\.\d{2})\](.*)/);
        if (match) {
            const minutes = parseInt(match[1]);
            const seconds = parseFloat(match[2]);
            return { time: (minutes * 60) + seconds, text: match[3].trim() };
        }
        return null;
    }).filter(item => item !== null && item.text !== "");
}

// Sync theo player
function startLyricSync() {
    const player = document.getElementById('mainPlayer');
    if (!player) return;

    player.ontimeupdate = () => {
        const currentTime = player.currentTime;
        const currentIndex = lyricsData.findIndex((line, index) => {
            const nextLine = lyricsData[index + 1];
            return currentTime >= line.time && (!nextLine || currentTime < nextLine.time);
        });

        if (currentIndex !== -1) {
            updateLyricUI(currentIndex);
        }
    };
}

// Cập nhật UI highlight
function updateLyricUI(index) {
    const lines = document.querySelectorAll('.lyric-line');
    lines.forEach((el, i) => {
        el.classList.toggle('active-lyric', i === index);
        if (i === index) el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    });
}

// Render lời ra container
function displayLyrics(container) {
    container.innerHTML = lyricsData
        .map(line => `<div class="lyric-line" style="transition: 0.3s; padding: 5px;">${line.text}</div>`)
        .join('');
}