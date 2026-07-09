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