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
            <%-- Nút thêm vào danh sách hàng chờ sử dụng AJAX thông qua HTMX --%>
            <button class="btn btn-outline-primary btn-sm fw-bold me-1" type="button"
                    hx-post="/api/queue/add" 
                    hx-vals='{"songId": "${song.id}"}' 
                    hx-swap="none"
                    hx-on::after-request="if(event.detail.successful){this.classList.remove('btn-outline-primary');this.classList.add('btn-success');this.innerHTML='✓ Đã thêm';this.disabled=true;let t=document.getElementById('queueToast');if(t&&window.bootstrap){bootstrap.Toast.getOrCreateInstance(t).show();}}else{alert('Lỗi gửi request lên Java!');}">
                + Thêm
            </button>

            <%-- Nút Xem lời tải nội dung động --%>
            <button class="btn btn-outline-primary btn-sm fw-bold" type="button"
                    hx-get="/api/songs/lyrics/${song.id}" 
                    hx-target="#lyric-text-box-${song.id}" 
                    hx-swap="innerHTML"
                    hx-on::before-request="
                        document.querySelectorAll('[id^=\'lyric-row-\']').forEach(r => r.style.display = 'none');
                        document.getElementById('lyric-row-${song.id}').style.display = 'table-row';
                        document.getElementById('lyric-text-box-${song.id}').innerHTML = 'Đang nạp lời bài hát...';
                    " 
                    hx-on::after-request="
                        if(event.detail.successful) {
                            let rawText = event.detail.xhr.responseText;
                            let lines = rawText.split('\n');
                            let cleanLyricsHTML = '';
                            
                            lines.forEach(line => {
                                let cleanLine = line.replace(/\[\d{2}:\d{2}(\.\d{2})?\]/g, '').trim();
                                if (cleanLine) {
                                    cleanLyricsHTML += cleanLine + '<br>';
                                }
                            });
                            document.getElementById('lyric-text-box-${song.id}').innerHTML = cleanLyricsHTML !== '' ? cleanLyricsHTML : 'Bài hát này hiện chưa có lời chi tiết.';
                        }
                    ">
                Xem lời
            </button>
        </td>
    </tr>

    <%-- Hàng ẩn chứa lời bài hát --%>
    <tr id="lyric-row-${song.id}" class="song-row" style="display: none;">
        <td colspan="5" class="p-0 border-0">
            <div class="p-4 text-center text-light border-start border-primary border-3 m-2 rounded"
                 style="font-size: 0.95rem; line-height: 2rem; background-color: #15181c !important;">

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-primary small fw-bold text-uppercase">Lời bài hát</span>
                    <button class="btn btn-sm btn-secondary fw-bold px-3" type="button"
                            onclick="document.getElementById('lyric-row-${song.id}').style.display = 'none'">
                        Thu gọn ▲
                    </button>
                </div>

                <div id="lyric-text-box-${song.id}" class="mb-4"></div>
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