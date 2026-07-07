<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    /* Tùy chỉnh thanh range */
input[type=range].form-range {
    -webkit-appearance: none;
    width: 100%;
    background: transparent;
}

/* Track: Phần nền (màu xanh nhạt) */
input[type=range].form-range::-webkit-slider-runnable-track {
    width: 100%;
    height: 6px;
    border-radius: 5px;
    background: #cce5ff; /* Xanh nhạt cho phần chưa phát */
    /* Đây là nơi chúng ta sẽ vẽ phần xanh đậm đè lên */
    background-image: linear-gradient(#007bff, #007bff); 
    background-repeat: no-repeat;
    background-size: var(--progress, 0%) 100%; /* Cập nhật biến này bằng JS */
}

/* Thumb: Nút tròn di chuyển */
input[type=range].form-range::-webkit-slider-thumb {
    -webkit-appearance: none;
    height: 16px;
    width: 16px;
    border-radius: 50%;
    background: #007bff; /* Màu xanh đậm cho nút */
    cursor: pointer;
    margin-top: -5px; /* Căn giữa track */
}
</style>
<%-- ==========================================
     1. MAIN PLAYER CONTAINER (Fixed Bottom)
     ========================================== --%>
<div id="player-container" class="fixed-bottom bg-body border-top border-secondary-subtle shadow-lg p-3">
    <div class="container-fluid d-flex align-items-center justify-content-between">

        <%-- 2. THÔNG TIN BÀI HÁT (Thumbnail & Info) --%>
        <div class="d-flex align-items-center w-25">
            <img src="${pageContext.request.contextPath}/images/thumbnail.jpg" id="songThumb" class="rounded" width="50" height="50" alt="Cover">
            <div class="ms-3 text-truncate">
                <div id="nowPlaying" class="fw-bold text-truncate">Chưa có bài hát</div>
                <div id="artistName" class="small text-body-secondary">Nghệ sĩ</div>
            </div>
        </div>

        <%-- 3. KHU VỰC ĐIỀU KHIỂN CHÍNH (Controls & Progress) --%>
        <div class="d-flex flex-column align-items-center w-50">
            <div class="d-flex align-items-center gap-3 mb-1">
                <button class="btn btn-link text-body" onclick="prevSong()">⏮</button>
                <button class="btn btn-link text-body fs-4" id="playPauseBtn" onclick="togglePlay()">⏯</button>
                <button class="btn btn-link text-body" onclick="nextSong()">⏭</button>
            </div>

            <div class="d-flex align-items-center w-100 gap-2">
                <span id="currentTime" class="small text-body-secondary">0:00</span>
                <input type="range" id="progressBar" class="form-range" value="0" min="0" max="100" oninput="seekSong(this.value)">
                <span id="duration" class="small text-body-secondary">0:00</span>
            </div>
        </div>

        <%-- 4. TIỆN ÍCH & MENU (Volume, Loop, Queue) --%>
        <div class="d-flex align-items-center gap-2 w-25 justify-content-end">
            <input type="range" id="volumeRange" min="0" max="1" step="0.1" value="1" oninput="setVolume(this.value)" style="width: 70px;">
            
            <button id="loopBtn" class="btn btn-sm btn-outline-secondary" onclick="toggleLoopMode()" title="Chế độ phát">⊘</button>
            <button id="lyricsBtn" class="btn btn-sm btn-outline-secondary" onclick="toggleLyrics()" title="Lời bài hát">Lời</button>
            <div class="dropup">
                <button class="btn btn-sm btn-outline-secondary" onclick="addAllToQueue()" title="Thêm tất cả vào danh sách">
                    ++
                </button>
                <button class="btn btn-sm btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown">☰</button>
                <ul class="dropdown-menu dropdown-menu-end p-0" style="max-height: 300px; overflow-y: auto; width: 250px;">
                    <div id="queueDropdownList" class="p-2"></div>
                </ul>
            </div>
        </div>
    </div>

    <%-- 5. MEDIA CORE (Thẻ audio ẩn) --%>
    <audio id="mainPlayer"></audio>
</div>

<%-- ==========================================
     6. MODAL & OVERLAYS (Các thành phần phụ)
     ========================================== --%>
<div class="modal fade" id="lyricsModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header border-bottom">
                <h5 class="modal-title">Lời: <span id="lyricsTitle"></span></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-center" id="modalLyricsContent" style="max-height: 400px; overflow-y: auto; white-space: pre-line;">
                Vui lòng chọn bài hát...
            </div>
        </div>
    </div>
</div>

<%-- TOAST NOTIFICATION (Cần thêm vào nếu trang chưa có) --%>
<div class="toast-container position-fixed top-0 start-50 translate-middle-x p-3" style="z-index: 1055; margin-top: 20px;">
    <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div id="toastMessage" class="toast-body">
            </div>
    </div>
</div>
