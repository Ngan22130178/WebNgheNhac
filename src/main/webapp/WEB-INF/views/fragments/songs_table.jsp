<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
   
    /* Ẩn chữ mặc định trong button có class này */
    .btn-hover-reveal .text-label {
        display: none;
        margin-left: 6px; /* Khoảng cách giữa icon và chữ */
    }

    /* Hiện chữ khi hover vào button */
    .btn-hover-reveal:hover .text-label {
        display: inline;
    }

</style>

<c:forEach var="song" items="${songs}">
    <%-- Xử lý an toàn cho dữ liệu --%>
    <c:set var="safeTitle" value="${fn:escapeXml(song.title)}" />
    <c:set var="safeUrl" value="${fn:escapeXml(song.url)}" />

    <%-- Hàng hiển thị thông tin bài hát --%>
    <tr class="song-row">
        <td class="align-middle" style="cursor: pointer; color: #007bff;"
            onclick="playNow('${safeUrl}', '${safeTitle}', '${fn:escapeXml(song.artist)}')">
            ${song.title}
        </td>
        <td class="align-middle">${song.artist}</td>
        <td class="align-middle">
            <span class="badge bg-primary">${song.genre}</span>
        </td>
        <td class="align-middle">${song.albumName}</td>
        
        <td class="align-middle">
            <div class="btn-group" role="group">
            <%-- Thêm bài hát vào danh sách đang phát --%>
            <button class="btn btn-outline-primary btn-sm fw-bold"
                    data-bs-toggle="tooltip" 
                    data-bs-placement="top" 
                    title="Thêm vào danh sách đang phát"
                    onclick="addToQueue('${safeUrl}', '${safeTitle}', '${fn:escapeXml(song.artist)}')">
                <i class="fa-solid fa-plus"></i>
            </button>
            <%-- Xem lời bài hát --%>
            <button class="btn btn-outline-info btn-sm" 
                    type="button"
                    data-bs-toggle="tooltip" 
                    data-bs-placement="top" 
                    title="Xem lời bài hát"
                    onclick="loadAndParseLyrics('${song.id}', null)"> 
                <i class="fa-solid fa-align-left"></i>
            </button>
        </div>
    </td>
    </tr>

    <%-- Hàng ẩn chứa lời bài hát --%>
    <tr id="lyric-row-${song.id}" class="song-row-lyric" style="display: none;">
        <td colspan="5" class="p-0 border-0">
            <div class="p-3 m-2 border-start border-info border-4 rounded shadow-sm" 
                style="background-color: var(--bs-info-bg-subtle);">
                
                <%-- Header vùng lời --%>
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="badge bg-info text-dark text-uppercase">
                        <i class="fa-solid fa-microphone-lines me-1"></i>Lời bài hát
                    </span>
                    <button class="btn btn-sm btn-outline-dark" 
                            onclick="document.getElementById('lyric-row-${song.id}').style.display = 'none'">
                        <i class="fa-solid fa-chevron-up"></i> Thu gọn
                    </button>
                </div>
                
                <%-- Vùng chứa text lời - Tối ưu cho hiển thị dài --%>
                <div id="lyric-text-box-${song.id}" 
                    class="lyric-content-area bg-white p-3 rounded border shadow-inner" 
                    style="font-size: 1rem; line-height: 1.6; white-space: pre-wrap; max-height: 400px; overflow-y: auto;">
                    <em class="text-muted">Đang tải lời...</em>
                </div>
            </div>
        </td>
    </tr>
</c:forEach>

<%-- Trường hợp không có dữ liệu --%>
<c:if test="${empty songs}">
    <tr class="song-row">
        <td colspan="5" class="text-center text-muted py-4">
            <c:choose>
                <c:when test="${not empty keyword}">
                    ❌ Không có kết quả cho: "<strong>${fn:escapeXml(keyword)}</strong>"
                </c:when>
                <c:otherwise>
                    🎵 Danh sách nhạc hiện đang trống.
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</c:if>

<script>
    // Kích hoạt toàn bộ tooltip trên trang
    document.addEventListener("DOMContentLoaded", function() {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    });
</script>
<script src="js/player-ui.js"></script>
