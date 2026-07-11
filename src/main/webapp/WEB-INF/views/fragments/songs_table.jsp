<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .btn-hover-reveal .text-label {
        display: none;
        margin-left: 6px;
    }

    .btn-hover-reveal:hover .text-label {
        display: inline;
    }
</style>

<c:set var="tableRows">
    <c:forEach var="song" items="${songs}">
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

                    <!-- TRƯỜNG HỢP 1: BÊN TRANG BÀI HÁT YÊU THÍCH -->
                    <c:if test="${pageType == 'favorite'}">
                        <button class="btn btn-outline-primary btn-sm fw-bold"
                            onclick="addToQueue('${safeUrl}', '${safeTitle}', '${fn:escapeXml(song.artist)}')">
                            <i class="fa-solid fa-plus"></i> 
                        </button>
                        <button class="btn btn-outline-info btn-sm"
                            onclick="loadAndParseLyrics('${song.id}', null)">
                            <i class="fa-solid fa-align-left"></i> Lời
                        </button>
                        <button class="btn btn-outline-danger btn-sm"
                            hx-post="/user/api/user/favorite/toggle"
                            hx-vals='{"songId": "${song.id}"}' hx-target="closest tr"
                            hx-swap="outerHTML">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </c:if>

                    <!-- TRƯỜNG HỢP 2: BÊN TRANG PLAYLIST HOẶC TRANG CHỦ -->
                    <c:if test="${pageType != 'favorite'}">
                        <div id="home-fav-container-${song.id}"
                            style="display: inline-block; vertical-align: middle; margin-right: 8px;">
                            <button class="btn btn-link p-0 border-0 m-0"
                                style="color: #ff0000; font-size: 1.2rem;"
                                hx-post="/user/api/user/favorite/toggle"
                                hx-vals='{"songId": "${song.id}"}'
                                hx-target="#home-fav-container-${song.id}" hx-swap="outerHTML">
                                <i class="fa-solid fa-heart"></i>
                            </button>
                        </div>
                        <button class="btn btn-outline-primary btn-sm fw-bold"
                            onclick="addToQueue('${safeUrl}', '${safeTitle}', '${fn:escapeXml(song.artist)}')">
                            <i class="fa-solid fa-plus"></i> 
                        </button>
                        <button class="btn btn-outline-info btn-sm"
                            onclick="loadAndParseLyrics('${song.id}', null)">
                            <i class="fa-solid fa-align-left"></i> Lời
                        </button>
                    </c:if>

                </div>
            </td>
        </tr>

        <%-- Hàng ẩn chứa lời bài hát --%>
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
                            <i class="fa-solid fa-chevron-up"></i> Thu gọn
                        </button>
                    </div>

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
</c:set>

<c:choose>
    <c:when test="${isProfilePage == true}">
        <div class="table-responsive bg-white rounded shadow-sm">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <th scope="col" style="width: 30%;">Tên bài hát</th>
                        <th scope="col" style="width: 20%;">Ca sĩ</th>
                        <th scope="col" style="width: 15%;">Thể loại</th>
                        <th scope="col" style="width: 15%;">Album</th>
                        <th scope="col" class="text-center" style="width: 20%;">Nút</th>
                    </tr>
                </thead>
                <tbody>
                    ${tableRows}
                </tbody>
            </table>
        </div>
    </c:when>

    <c:otherwise>
        ${tableRows}
    </c:otherwise>
</c:choose>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    });
</script>
<script src="js/player-ui.js"></script>