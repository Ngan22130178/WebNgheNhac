<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MusicWeb - Thế giới âm nhạc</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        /* [LAYOUT FIX: Đảm bảo footer luôn ở dưới và không bị che bởi player] */
        html, body { height: 100%; margin: 0; }
        body { 
            display: flex; 
            flex-direction: column; 
            transition: background-color 0.3s, color 0.3s; 
        }
        /* Đảm bảo trình phát không bị che khuất bởi các phần tử khác, main tự giãn nở */
        main { flex: 1; padding-bottom: 100px; } 
        
        .htmx-hidden { display: none !important; }
    </style>
</head>

<body>

    <%-- 1. HEADER --%>
    <jsp:include page="fragments/header.jsp" />

    <%-- 2. MAIN CONTENT (Tự đẩy footer xuống) --%>
    <main id="content" class="container mt-4">
        <c:choose>
            <c:when test="${not empty songs}">
                <table class="table table-hover table-striped" id="songTable">
                    <thead class="table-dark">
                        <tr>
                            <th onclick="handleSort(0)" style="cursor: pointer;">Tên bài hát <span id="sort-icon-0"><i class="fa-solid fa-sort text-muted ms-1"></i></span></th>
                            <th onclick="handleSort(1)" style="cursor: pointer;">Ca sĩ <span id="sort-icon-1"><i class="fa-solid fa-sort text-muted ms-1"></i></span></th>
                            <th>Thể loại</th>
                            <th>Album</th>
                            <th>Nút</th>
                        </tr>
                    </thead>
                    <tbody id="songListBody">
                        <jsp:include page="fragments/songs_table.jsp" />
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div id="songListBody">
                    <!-- Trường hợp danh sách trống -->
                    <div class="card shadow-sm border-0 w-100 text-center text-muted py-5">
                        <i class="fa-solid fa-music mb-2 fa-2x d-block text-secondary"></i>
                        🎵 Danh sách nhạc hiện đang trống.
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
 
    <%-- 3. FOOTER & PLAYER (Player nằm ngoài luồng main để fix bottom) --%>
    <jsp:include page="fragments/footer.jsp" />
    <jsp:include page="fragments/player.jsp" />

    <%-- 4. EXTERNAL SCRIPTS --%>
    <script src="https://unpkg.com/htmx.org@2.0.0"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <%-- 5. MODULE SCRIPTS (Thứ tự nạp quan trọng) --%>
    <script> document.addEventListener('DOMContentLoaded', () => { initTheme(); }); </script>
    <script src="js/player-state.js"></script>
    <script src="js/player-core.js"></script>
    <script src="js/player-dispatcher.js"></script>
    <script src="js/player-strategies.js"></script>
    <script src="js/player-playback.js"></script>
    <script src="js/player-ui.js"></script>
    <script src="js/ui-helper.js"></script>
    <script src="js/lyrics-handler.js"></script>
    <script src="/js/player.js"></script> 

    <%-- 6. LOGIC & HTMX HANDLING --%>
    <script>
        let originalPairs = null; // Lưu thứ tự cặp hàng mặc định
        let currentSortState = 0; // 0: Mặc định, 1: A-Z, 2: Z-A

        function handleSort(columnIndex) {
            const tbody = document.getElementById('dynamicSongRows') || document.getElementById('songListBody');
            if (!tbody) return;

            // Lấy tất cả các dòng đang có trong tbody
            const allRows = Array.from(tbody.querySelectorAll('tr.song-row'));
            if (allRows.length === 0) return;

            // Nhóm các hàng thành từng cặp: [Hàng thông tin, Hàng lời bài hát]
            const pairs = [];
            for (let i = 0; i < allRows.length; i += 2) {
                if (allRows[i] && allRows[i+1]) {
                    pairs.push([allRows[i], allRows[i+1]]);
                }
            }

            // Lưu lại trật tự gốc nếu là lần bấm đầu tiên
            if (!originalPairs) {
                originalPairs = [...pairs];
            }

            // Đổi trạng thái xoay vòng: 0 -> 1 -> 2 -> 0
            currentSortState = (currentSortState + 1) % 3;
            const iconSpan = document.getElementById(`sort-icon-${columnIndex}`);

            // Reset icon của cột còn lại về mặc định
            const otherIndex = columnIndex === 0 ? 1 : 0;
            const otherIcon = document.getElementById(`sort-icon-${otherIndex}`);
            if (otherIcon) otherIcon.innerHTML = '<i class="fa-solid fa-sort text-muted ms-1"></i>';

            if (currentSortState === 1) {
                // Click lần 1: Sắp xếp từ A đến Z
                sortPairsAscending(pairs, columnIndex);
                renderPairs(tbody, pairs);
                if (iconSpan) iconSpan.innerHTML = '<i class="fa-solid fa-sort-up text-primary ms-1"></i>';
            } 
            else if (currentSortState === 2) {
                // Click lần 2: Sắp xếp từ Z đến A
                sortPairsDescending(pairs, columnIndex);
                renderPairs(tbody, pairs);
                if (iconSpan) iconSpan.innerHTML = '<i class="fa-solid fa-sort-down text-primary ms-1"></i>';
            } 
            else {
                // Click lần 3: Quay về mặc định ban đầu
                renderPairs(tbody, originalPairs);
                if (iconSpan) iconSpan.innerHTML = '<i class="fa-solid fa-sort text-muted ms-1"></i>';
            }
        }

        // Hàm sắp xếp tăng dần (A-Z)
        function sortPairsAscending(pairs, index) {
            pairs.sort((pairA, pairB) => {
                const cellA = pairA[0].cells[index].innerText.trim().toLowerCase();
                const cellB = pairB[0].cells[index].innerText.trim().toLowerCase();
                return cellA.localeCompare(cellB, 'vi', { sensitivity: 'base' });
            });
        }

        // Hàm sắp xếp giảm dần (Z-A)
        function sortPairsDescending(pairs, index) {
            pairs.sort((pairA, pairB) => {
                const cellA = pairA[0].cells[index].innerText.trim().toLowerCase();
                const cellB = pairB[0].cells[index].innerText.trim().toLowerCase();
                return cellB.localeCompare(cellA, 'vi', { sensitivity: 'base' });
            });
        }

        // Hàm cập nhật lại DOM giao diện sau khi sort
        function renderPairs(tbody, pairs) {
            pairs.forEach(pair => {
                tbody.appendChild(pair[0]); // Đẩy hàng thông tin vào trước
                tbody.appendChild(pair[1]); // Đẩy hàng chứa lời bài hát vào sau
            });
        }

        // GỘP CHUNG TẤT CẢ LOGIC LẮNG NGHE HTMX ĐỂ KHÔNG BỊ XUNG ĐỘT
        document.body.addEventListener('htmx:afterOnLoad', function(evt) {
            console.log("HTMX load thành công");

            // 1. CHỈ lọc thời gian khi HTMX vừa đổ dữ liệu vào đúng ô lyric
            const targetId = evt.detail.target ? evt.detail.target.id : '';
            if (targetId && targetId.startsWith('lyric-text-box-')) {
                const lyricBox = document.getElementById(targetId);
                if (lyricBox) {
                    let rawLyrics = lyricBox.innerHTML;
                    // Xóa các đoạn mã thời gian [00:12.30]
                    let cleanLyrics = rawLyrics.replace(/\[\d{2}:\d{2}(?:\.\d{2,3})?\]/g, '');
                    lyricBox.innerHTML = cleanLyrics;
                }
            }

            // 2. Tự động reset bộ nhớ Sort khi HTMX thay đổi danh sách bài hát (Tìm kiếm, đổi tab...)
            originalPairs = null;
            currentSortState = 0;
        });

        document.body.addEventListener('htmx:responseError', function(evt) {
            console.error("HTMX lỗi:", evt.detail.xhr.status, evt.detail.xhr.responseText);
        });
    </script>
</body>
</html>