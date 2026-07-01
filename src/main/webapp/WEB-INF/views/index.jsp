<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MusicWeb - Thế giới âm nhạc</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        body { transition: background-color 0.3s, color 0.3s; }
        .htmx-hidden { display: none !important; }
        /* Đảm bảo trình phát không bị che khuất bởi các phần tử khác */
        main { padding-bottom: 100px; } 
        
    </style>
</head>

<body>

    <jsp:include page="fragments/header.jsp" />


    <main id="content" class="container mt-4">
        <table class="table table-hover table-striped" id="songTable">
            <thead class="table-dark">
                <tr>
                    <th>Tên bài hát</th>
                    <th>Ca sĩ</th>
                    <th>Thể loại</th>
                    <th>Album</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <%-- HTMX nạp dữ liệu vào bảng khi trang load xong --%>
            <tbody id="songListBody">
                <c:choose>
                    <c:when test="${not empty songs}">
                        <%-- Gọi file fragment trực tiếp tại đây --%>
                        <jsp:include page="fragments/songs_table.jsp" />
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="5" class="text-center">Danh sách trống...</td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </main>
 
    <jsp:include page="fragments/footer.jsp" />
    <jsp:include page="fragments/player.jsp" />

    <script src="https://unpkg.com/htmx.org@2.0.0"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script src="/js/ui-helper.js"></script>
    <script>
        // Đảm bảo hàm này được gọi khi DOM đã sẵn sàng
        document.addEventListener('DOMContentLoaded', () => {
            initTheme();
        });
    </script>
    <script src="/js/player.js"></script> 
    <script>
    document.body.addEventListener('htmx:afterOnLoad', function(evt) {
        console.log("HTMX load thành công:", evt.detail.xhr.responseText);
    });
    document.body.addEventListener('htmx:responseError', function(evt) {
        console.error("HTMX lỗi:", evt.detail.xhr.status, evt.detail.xhr.responseText);
    });
</script>
</body>
</html>