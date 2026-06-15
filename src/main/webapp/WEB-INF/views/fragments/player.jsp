<%-- fragments/player.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="player-container" class="fixed-bottom bg-dark text-white p-3 border-top border-secondary">
    <div class="container-fluid d-flex align-items-center justify-content-between">
        
        <div class="d-flex align-items-center w-25">
            <img src="path/to/thumbnail.jpg" id="songThumb" class="rounded" width="50" height="50" alt="Cover">
            <div class="ms-3 text-truncate">
                <div id="nowPlaying" class="fw-bold">Chưa có bài hát</div>
                <div id="artistName" class="small text-muted">Nghệ sĩ</div>
            </div>
        </div>

        <div class="d-flex flex-column align-items-center w-50">
            <div class="d-flex align-items-center gap-3 mb-1">
                <button class="btn btn-link text-white" onclick="prevSong()">⏮</button>
                <button class="btn btn-link text-white fs-4" id="playPauseBtn" onclick="togglePlay()">⏯</button>
                <button class="btn btn-link text-white" onclick="nextSong()">⏭</button>
            </div>
            
            <div class="d-flex align-items-center w-100 gap-2">
                <span id="currentTime" class="small">0:00</span>
                <input type="range" id="progressBar" class="form-range" value="0" min="0" max="100" oninput="seekSong(this.value)">
                <span id="duration" class="small">0:00</span>
            </div>
        </div>

        <div class="d-flex align-items-center gap-2 w-25 justify-content-end">
            <input type="range" id="volumeRange" min="0" max="1" step="0.1" value="1" oninput="setVolume(this.value)" style="width: 80px;">
            <button id="loopBtn" class="btn btn-sm btn-outline-light" onclick="toggleLoopMode()" title="Chế độ phát">🔀</button>

            
            <div class="dropup">
                <button class="btn btn-sm btn-outline-light dropdown-toggle" data-bs-toggle="dropdown" onclick="loadLyricsMenu()">Lời</button>
                <ul class="dropdown-menu dropdown-menu-dark" id="lyricsMenu">
                    <li class="px-3 py-2 text-muted">Đang tải...</li>
                </ul>
            </div>

            
            <div class="dropup">
                <button class="btn btn-sm btn-outline-info dropdown-toggle" data-bs-toggle="dropdown" onclick="updateQueueMenu()">☰</button>
                <ul class="dropdown-menu dropdown-menu-dark p-0" style="max-height: 300px; overflow-y: auto; width: 250px;">
                    <div id="queueDropdownList"></div>
                </ul>
            </div>
        </div>
    </div>
    
    <audio id="mainPlayer" class="d-none"></audio>
</div>

<%-- Chỉ giữ lại phần hiển thị lời trong Modal --%>
<div class="modal fade" id="lyricsModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content bg-dark text-white">
            <div class="modal-header border-secondary">
                <h5 class="modal-title">Lời: <span id="lyricsTitle"></span></h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <%-- Đổi thành id="modalLyricsContent" để tránh trùng lặp --%>
            <div class="modal-body text-center" id="modalLyricsContent" 
                 style="max-height: 400px; overflow-y: auto; white-space: pre-line; color: white;">
                Vui lòng chọn bài hát...
            </div>
        </div>
    </div>
</div>
</div>