<%-- fragments/songs_table.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach var="song" items="${songs}">
    <tr>
        <td class="align-middle" style="cursor: pointer; color: #007bff;"
            onclick="playNow('${song.url}', '${song.title}')">
            ${song.title}
        </td>
        <td class="align-middle">${song.artist}</td>
        <td class="align-middle"><span class="badge bg-primary">${song.genre}</span></td>
        <td class="align-middle">${song.albumName}</td>
        <td class="align-middle">
            <button class="btn btn-outline-primary btn-sm fw-bold me-1">
                + Thêm
            </button>

            <button class="btn btn-outline-primary btn-sm fw-bold" 
                    type="button"
                    hx-get="/api/lyrics/${song.id}"
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

    <tr id="lyric-row-${song.id}" style="display: none;">
        <td colspan="5" class="p-0 border-0">
            <div class="p-4 text-center text-light border-start border-primary border-3 m-2 rounded" 
                 style="font-size: 0.95rem; line-height: 2rem; background-color: #15181c !important;">
                
                <div class="text-primary small fw-bold mb-3"> LỜI BÀI HÁT </div>
                
                <div id="lyric-text-box-${song.id}" class="mb-4"></div>
                
                <button class="btn btn-sm btn-secondary fw-bold px-4 py-1.5 mt-2" 
                        type="button"
                        onclick="document.getElementById('lyric-row-${song.id}').style.display = 'none'">
                    Thu gọn ▲
                </button>
            </div>
        </td>
    </tr>
</c:forEach>

<c:if test="${empty songs && not empty keyword}">
    <tr>
        <td colspan="5" class="text-center text-warning py-4">
            ❌ Không có bài hát phù hợp với từ khóa: "<strong>${keyword}</strong>"
        </td>
    </tr>
</c:if>