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
        body { 
            transition: background-color 0.3s, color 0.3s; 
        }
        
        body.bg-dark {
            background-color: #212529;
            color: #fff;
        }

        body.bg-dark .table {
            color: #fff;
            border-color: #555;
        }

        #songTable {
            border-bottom: none !important;
            width: 100% !important;
            table-layout: auto !important;
        }

        #songTable tbody tr:last-child td {
            border-bottom: none !important;
        }

        .htmx-hidden { 
            display: none !important; 
        }
        
        /* Đảm bảo trình phát không bị che khuất bởi các phần tử khác */
        main { 
            padding-bottom: 100px; 
        } 
    </style>
</head>

<body class="bg-dark">

    <div id="header">
        <jsp:include page="fragments/header.jsp" />
    </div>

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
            <%-- Kiểm tra dữ liệu bài hát để render hoặc thông báo trống --%>
            <tbody id="songListBody">
                <c:choose>
                    <c:when test="${not empty songs}">
                        <jsp:include page="fragments/songs_table.jsp" />
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="text-center text-muted py-4">
                                🎵 Danh sách nhạc hiện đang trống hoặc đang nạp...
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </main>

    <%-- Thông báo Toast khi thêm vào danh sách chờ thành công --%>
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 11">
        <div id="queueToast" class="toast align-items-center text-white bg-success border-0" role="alert"
             aria-live="assertive" aria-atomic="true" data-bs-delay="2500"> 
            <div class="d-flex">
                <div class="toast-body fw-bold">
                    🎵 Đã thêm bài hát vào danh sách chờ thành công!
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>

    <%-- Các thành phần giao diện Player & Footer --%>
    <jsp:include page="fragments/player.jsp" />
    <jsp:include page="fragments/footer.jsp" />

    <%-- Tải Thư viện JavaScript --%>
    <script src="https://unpkg.com/htmx.org@2.0.0"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <%-- Script điều khiển của ứng dụng --%>
    <script src="/js/player-core.js"></script>
    <script src="/js/player-controls.js"></script>
    <script src="/js/player.js"></script> 
    <script src="/js/ui-helper.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // Khởi tạo trạng thái Toast
            var toastEl = document.getElementById('queueToast');
            if (toastEl && typeof bootstrap !== 'undefined') {
                bootstrap.Toast.getOrCreateInstance(toastEl, { delay: 2000 });
            }
            
            // Khởi tạo theme (giao diện sáng/tối)
            if (typeof initTheme === 'function') {
                initTheme();
            }
        });

        // Lắng nghe các sự kiện hỗ trợ debug lỗi HTMX
        document.body.addEventListener('htmx:afterOnLoad', function(evt) {
            console.log("HTMX load thành công:", evt.detail.xhr.responseText);
        });
        document.body.addEventListener('htmx:responseError', function(evt) {
            console.error("HTMX lỗi:", evt.detail.xhr.status, evt.detail.xhr.responseText);
        });
    </script>
</body>
</html>