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

