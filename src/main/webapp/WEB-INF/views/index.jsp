<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
    <script src="https://unpkg.com/htmx.org@2.0.0"></script>
    <title>MusicWeb - Thế giới âm nhạc</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
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
    </style>
</head>

<body class="bg-dark"> <div id="header">
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
                    <th>Phát nhạc</th>
                </tr>
            </thead>
            <tbody id="songListBody">
                <jsp:include page="fragments/songs_table.jsp" />
            </tbody>
        </table>
    </main>

    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 11">
        <div id="queueToast" class="toast align-items-center text-white bg-success border-0" role="alert"
            aria-live="assertive" aria-atomic="true" data-bs-delay="2500"> <div class="d-flex">
                <div class="toast-body fw-bold">
                    🎵 Đã thêm bài hát vào danh sách chờ thành công!
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                    aria-label="Close"></button>
            </div>
        </div>
    </div>

    <jsp:include page="fragments/player.jsp" />

    <jsp:include page="fragments/footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="/js/player-core.js"></script>
    <script src="/js/player-controls.js"></script>
    <script src="/js/ui-helper.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var toastEl = document.getElementById('queueToast');
            if (toastEl && typeof bootstrap !== 'undefined') {
                bootstrap.Toast.getOrCreateInstance(toastEl, { delay: 2000 });
            }
        });
    </script>

</body>

</html>