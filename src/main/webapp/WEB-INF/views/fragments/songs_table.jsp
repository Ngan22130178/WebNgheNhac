<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    /* Hiệu ứng hover cho các nút nếu cần dùng class này */
    .btn-hover-reveal .text-label {
        display: none;
        margin-left: 6px;
    }
    .btn-hover-reveal:hover .text-label {
        display: inline;
    }
    
    /* Đảm bảo vùng hiển thị lời bài hát đẹp mắt */
    .lyric-content-area::-webkit-scrollbar {
        width: 6px;
    }
    .lyric-content-area::-webkit-scrollbar-thumb {
        background-color: rgba(0, 0, 0, 0.2);
        border-radius: 4px;
    }
</style>

<!-- KHUNG BẢNG ĐẦY ĐỦ CHUẨN CẤU TRÚC DOM -->
<div class="card shadow-sm border-0 w-100">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">

            <!-- 🔥 SỬA LỖI MẤT THANH ĐEN: Chỉ hiển thị thead khi ở trong trang Profile -->
            <c:if test="${isProfilePage == true}">
                <thead class="table-dark">
                    <tr>
                        <th scope="col" style="padding-left: 15px;">Tên bài hát</th>
                        <th scope="col">Ca sĩ</th>
                        <th scope="col">Thể loại</th>
                        <th scope="col">Album</th>
                        <th scope="col" class="text-center">Tính năng</th>
                    </tr>
                </thead>
            </c:if>

            <!-- ĐẶT ID CHÍNH XÁC Ở THẺ TBODY ĐỂ HTMX BÓC TÁCH KHÔNG BỊ LẶP -->
            <tbody id="dynamicSongRows">
                <c:choose>
                    <c:when test="${not empty songs}">
                        <c:forEach var="song" items="${songs}">
                            <%-- Định nghĩa TẤT CẢ các biến an toàn để tránh lỗi XSS hoặc lỗi cú pháp JS khi chuỗi chứa dấu nháy --%>
                            <c:set var="safeTitle" value="${fn:escapeXml(song.title)}" />
                            <c:set var="safeUrl" value="${fn:escapeXml(song.url)}" />
                            <c:set var="safeArtist" value="${fn:escapeXml(song.artist)}" />

                            <!-- HÀNG HIỂN THỊ THÔNG TIN BÀI HÁT -->
                            <tr class="song-row">
                                <td class="align-middle fw-semibold"
                                    style="cursor: pointer; color: #007bff; padding-left: 15px;"
                                    onclick="playNow('${safeUrl}', '${safeTitle}', '${safeArtist}')"
                                    data-bs-toggle="tooltip" title="Click để phát ngay">
                                    ${song.title}
                                </td>
                                <td class="align-middle text-secondary">${song.artist}</td>
                                <td class="align-middle">
                                    <span class="badge bg-primary px-2 py-1">${song.genre}</span>
                                </td>
                                <td class="align-middle text-muted">${song.albumName}</td>

                                <td class="align-middle">
                                    <div class="d-flex align-items-center justify-content-center gap-2">

                                        <!-- NÚT TRÁI TIM -->
                                        <c:if test="${isProfilePage != true}">
                                            <div id="home-fav-container-${song.id}" style="display: inline-block; vertical-align: middle;">
                                                <button class="btn btn-link p-0 border-0 m-0"
                                                        style="text-decoration: none; color: #ff0000; font-size: 1.2rem; line-height: 1;"
                                                        hx-post="/api/user/favorite/toggle"
                                                        hx-vals='{"songId": "${song.id}"}'
                                                        hx-target="#home-fav-container-${song.id}"
                                                        hx-swap="none"
                                                        data-bs-toggle="tooltip" title="Yêu thích"
                                                        onclick="toggleHeartUI('${song.id}', '${safeTitle}', '${safeArtist}')">
                                                    <i class="fa-solid fa-heart"></i>
                                                </button>
                                            </div>
                                        </c:if>

                                        <!-- NÚT THÊM VÀO DANH SÁCH ĐANG PHÁT / PLAYLIST -->
                                        <button class="btn btn-outline-primary btn-sm fw-bold"
                                                hx-post="/api/user/playlist/add"
                                                hx-vals='{"songId": "${song.id}"}' hx-swap="none"
                                                data-bs-toggle="tooltip" title="Thêm vào danh sách đang phát"
                                                onclick="addToQueue('${safeUrl}', '${safeTitle}', '${safeArtist}')">
                                            <i class="fa-solid fa-plus me-1"></i>Thêm
                                        </button>

                                        <!-- NÚT XEM LỜI BÀI HÁT -->
                                        <button class="btn btn-outline-info btn-sm fw-bold"
                                                type="button" 
                                                hx-get="/api/songs/lyrics/${song.id}"
                                                hx-target="#lyric-text-box-${song.id}" hx-swap="innerHTML"
                                                data-bs-toggle="tooltip" title="Xem lời bài hát"
                                                onclick="document.getElementById('lyric-row-${song.id}').style.display = 'table-row'">
                                            <i class="fa-solid fa-align-left me-1"></i>Lời
                                        </button>

                                        <!--  NÚT THÙNG RÁC -->
                                        <c:if test="${isProfilePage == true}">
                                            <div id="profile-trash-container-${song.id}" style="display: inline-block; vertical-align: middle;">
                                                <button class="btn btn-link p-0 border-0 m-0 text-danger"
                                                        style="text-decoration: none; font-size: 1.2rem; line-height: 1;"
                                                        hx-post="/api/user/favorite/toggle"
                                                        hx-vals='{"songId": "${song.id}"}'
                                                        hx-target="closest tr" hx-swap="outerHTML swap:0.3s"
                                                        data-bs-toggle="tooltip" title="Xóa khỏi danh sách"
                                                        onclick="if(!confirm('Bạn có chắc muốn xóa bài hát này khỏi danh sách?')) { event.stopImmediatePropagation(); event.preventDefault(); }">
                                                    <i class="fa-solid fa-trash-can"></i>
                                                </button>
                                            </div>
                                        </c:if>

                                    </div>
                                </td>
                            </tr>

                            <!-- HÀNG ẨN CHỨA LỜI BÀI HÁT -->
                            <tr id="lyric-row-${song.id}" class="song-row-lyric" style="display: none;">
                                <td colspan="5" class="p-0 border-0">
                                    <div class="p-3 m-2 border-start border-info border-4 rounded shadow-sm"
                                         style="background-color: var(--bs-info-bg-subtle);">
                                        
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <span class="badge bg-info text-dark text-uppercase">
                                                <i class="fa-solid fa-microphone-lines me-1"></i>Lời bài hát
                                            </span>
                                            <button class="btn btn-sm btn-outline-dark"
                                                    onclick="document.getElementById('lyric-row-${song.id}').style.display = 'none'">
                                                <i class="fa-solid fa-chevron-up me-1"></i>Thu gọn ▲
                                            </button>
                                        </div>
                                        
                                        <div id="lyric-text-box-${song.id}"
                                             class="lyric-content-area bg-white p-3 rounded border shadow-inner text-center"
                                             style="font-size: 1rem; line-height: 1.8; white-space: pre-wrap; max-height: 400px; overflow-y: auto;">
                                            <em class="text-muted">Đang tải lời bài hát...</em>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="song-row">
                            <td colspan="5" class="text-center text-muted py-5">
                                <c:choose>
                                    <c:when test="${not empty keyword}">
                                        <i class="fa-solid fa-magnifying-glass mb-2 fa-2x d-block text-secondary"></i>
                                        ❌ Không có kết quả cho: "<strong>${fn:escapeXml(keyword)}</strong>"
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa-solid fa-music mb-2 fa-2x d-block text-secondary"></i>
                                        🎵 Danh sách nhạc hiện đang trống.
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
        
        document.body.addEventListener('htmx:afterSwap', function(evt) {
            var oldTooltips = document.querySelectorAll('.tooltip.show, .tooltip.bs-tooltip-auto');
            oldTooltips.forEach(function(t) { t.remove(); });

            var newTooltips = evt.detail.target.querySelectorAll('[data-bs-toggle="tooltip"]');
            newTooltips.forEach(function (el) {
                return new bootstrap.Tooltip(el);
            });
        });

        document.addEventListener('click', function (e) {
            var clickedBtn = e.target.closest('[data-bs-toggle="tooltip"]');
            if (clickedBtn) {
                var instance = bootstrap.Tooltip.getInstance(clickedBtn);
                if (instance) {
                    instance.hide();
                }
                clickedBtn.blur();
            }
        });
    });
</script>

<script src="js/player-ui.js"></script>