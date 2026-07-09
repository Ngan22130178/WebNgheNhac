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
        main { flex: 1; padding-bottom: 20px; } /* main tự giãn nở */
        
        .htmx-hidden { display: none !important; }
    </style>
</head>

<body>

    <%-- 1. HEADER --%>
    <jsp:include page="fragments/header.jsp" />

    <%-- 2. MAIN CONTENT (Tự đẩy footer xuống) --%>
    <main id="content" class="container mt-4">
        <table class="table table-hover table-striped" id="songTable">
            <thead class="table-dark">
                <tr>
                    <th>Tên bài hát</th>
                    <th>Ca sĩ</th>
                    <th>Thể loại</th>
                    <th>Album</th>
                    <th>Nút</th>
                </tr>
            </thead>
            <tbody id="songListBody">
                <c:choose>
                    <c:when test="${not empty songs}">
                        <jsp:include page="fragments/songs_table.jsp" />
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="5" class="text-center">Danh sách trống...</td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
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

    <%-- 6. HTMX LOGGING --%>
    <script>
        document.body.addEventListener('htmx:afterOnLoad', (evt) => console.log("HTMX load ok"));
        document.body.addEventListener('htmx:responseError', (evt) => console.error("HTMX error"));
    </script>
</body>
</html>