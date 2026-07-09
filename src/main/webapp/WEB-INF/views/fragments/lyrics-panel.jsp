<div id="lyrics-window" class="floating-window" style="display: none;">
    <div class="window-header">
        <div class="header-controls">
            <div class="left-group">
                <button id="toggleFormatBtn" class="btn-ctrl" onclick="changeFormat()">LRC</button>
                <select id="langSelect" class="btn-ctrl" onchange="loadLyrics()">
                    </select>
            </div>
            <button class="btn-close" onclick="closeLyrics()">&#10005;</button>
        </div>
    </div>
    <div id="lyrics-content" class="window-body">
        <p>Đang tải...</p>
    </div>
</div>

<style>
    .floating-window {
        position: fixed; top: 100px; right: 20px;
        width: 350px; height: 500px;
        /* Hiệu ứng kính trong suốt */
        background: rgba(255, 255, 255, 0.15);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.3);
        border-radius: 12px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
        z-index: 9999;
        display: flex; flex-direction: column;
        color: white; overflow: hidden;
    }
    .header-controls {
        display: flex; justify-content: space-between; align-items: center;
        padding: 10px 15px; background: rgba(0, 0, 0, 0.2); cursor: move;
    }
    .left-group { display: flex; gap: 8px; }
    .btn-ctrl { 
        background: rgba(255,255,255,0.2); border: none; color: white;
        padding: 5px 10px; border-radius: 5px; cursor: pointer;
    }
    .btn-close { background: transparent; border: none; color: white; font-size: 18px; cursor: pointer; }
    .window-body { padding: 15px; overflow-y: auto; flex-grow: 1; font-size: 14px; text-shadow: 0 1px 2px black; }
</style>
<script>
    // Thay đổi định dạng và giữ nguyên ngôn ngữ đã chọn
    function changeFormat() {
        currentFormat = (currentFormat === 'LRC') ? 'TXT' : 'LRC';
        document.getElementById('toggleFormatBtn').innerText = currentFormat;
        // Gọi lại loadLyrics để lấy file mới với format mới
        loadLyrics(); 
    }

    // Load danh sách ngôn ngữ khi mở panel
    function openLyrics(songId) {
        currentSongId = songId;
        document.getElementById('lyrics-window').style.display = 'block';
        
        fetch(`/api/songs/${songId}/languages`)
            .then(res => res.json())
            .then(langs => {
                const select = document.getElementById('langSelect');
                select.innerHTML = langs.map(l => `<option value="${l}">${l.toUpperCase()}</option>`).join('');
                loadLyrics(); // Tự động load nội dung sau khi có danh sách ngôn ngữ
            });
    }
</script>