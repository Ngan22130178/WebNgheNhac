<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:forEach var="song" items="${songs}">
    <%-- Xử lý an toàn cho dữ liệu --%>
    <c:set var="safeTitle" value="${fn:escapeXml(song.title)}" />
    <c:set var="safeUrl" value="${fn:escapeXml(song.url)}" />

    <%-- Hàng hiển thị thông tin bài hát --%>
    <tr class="song-row">
        <td class="align-middle" style="cursor: pointer; color: #007bff;"
            onclick="playNow('${safeUrl}', '${safeTitle}')">
            ${song.title}
        </td>
        <td class="align-middle">${song.artist}</td>
        <td class="align-middle">
            <span class="badge bg-primary">${song.genre}</span>
        </td>
        <td class="align-middle">${song.albumName}</td>
        
        <td class="align-middle">
            <button class="btn btn-outline-primary btn-sm fw-bold me-1"
                onclick="addToQueue('${safeUrl}', '${safeTitle}')">
                + Thêm
            </button>

            <button class="btn btn-outline-primary btn-sm fw-bold" 
                    type="button"
                    hx-get="/api/songs/lyrics/${song.id}"
                    hx-target="#lyric-text-box-${song.id}"
                    hx-swap="innerHTML"
                    hx-on::before-request="showLyricRow('${song.id}')">
                Xem lời
            </button>
        </td>
    </tr>

    <%-- Hàng ẩn chứa lời bài hát --%>
    <tr id="lyric-row-${song.id}" class="song-row" style="display: none;">
        <td colspan="5" class="p-0 border-0">
            <div class="p-3 m-2 border-start border-primary border-3 rounded" 
                 style="background-color: var(--bs-tertiary-bg);">
                
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="text-primary small fw-bold text-uppercase">Lời bài hát</span>
                    <button class="btn btn-sm btn-outline-secondary" 
                            onclick="document.getElementById('lyric-row-${song.id}').style.display = 'none'">
                        Thu gọn ▲
                    </button>
                </div>
                
                <div id="lyric-text-box-${song.id}" class="text-muted" style="font-size: 0.95rem; white-space: pre-wrap;">
                    <em>Đang tải lời bài hát...</em>
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