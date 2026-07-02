<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>MusicWeb Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_theme.css">
</head>
<body>
<style>/* --- BIẾN MÀU SẮC (CSS VARIABLES) --- */
:root {
    /* Chế độ Sáng (Light Mode) */
    --bg-body: #F8F9FA;
    --sidebar-bg: #C1121F;      /* Màu Ruby */
    --card-bg: #FFFFFF;
    --text-main: #212529;
    --accent-orange: #F4A261;   /* Màu Cam */
    --border-color: #dee2e6;
}

body.dark-mode {
    /* Chế độ Tối (Dark Mode) */
    --bg-body: #121212;
    --sidebar-bg: #8B0000;      /* Ruby đậm hơn */
    --card-bg: #1E1E1E;
    --text-main: #E0E0E0;
    --accent-orange: #FF8C00;   /* Cam sáng hơn */
    --border-color: #333333;
}

/* --- CẤU TRÚC CHUNG --- */
body {
    background-color: var(--bg-body);
    color: var(--text-main);
    transition: background-color 0.3s, color 0.3s;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* --- SIDEBAR --- */

.sidebar .nav-link {
    color: rgba(255, 255, 255, 0.8);
    padding: 12px 20px;
    border-radius: 8px;
    transition: 0.2s;
}

.sidebar .nav-link:hover, .sidebar .nav-link.active {
    background-color: rgba(255, 255, 255, 0.2);
    color: #ffffff;
}
.sidebar {
    background-color: var(--sidebar-bg);
    position: fixed;       /* Cố định vị trí */
    top: 0;
    left: 0;
    height: 100vh;         /* Chiều cao bằng 100% màn hình */
    width: 260px;
    z-index: 1000;         /* Đảm bảo luôn nằm trên cùng */
    transition: background-color 0.3s;
}

/* Đẩy nội dung bên phải sang phải để không bị Sidebar che */
main {
    margin-left: 260px;    
}

/* --- THÀNH PHẦN (CARD & BUTTON) --- */
.card {
    background-color: var(--card-bg) !important;
    border: 1px solid var(--border-color) !important;
    transition: background-color 0.3s;
}

.btn-accent {
    background-color: var(--accent-orange) !important;
    border: none;
    transition: 0.3s;
}

.btn-accent:hover {
    filter: brightness(1.1);
}

/* --- TÙY CHỈNH BẢNG (TABLE) --- */
.table {
    color: var(--text-main) !important;
}

.table-light {
    background-color: var(--card-bg) !important;
    color: var(--text-main) !important;
}

/* Bo góc tất cả các element để giao diện trẻ trung */
.card, .btn, .form-control {
    border-radius: 10px !important;
}
</style>
<div class="d-flex">
    <aside class="sidebar p-3 d-flex flex-column">
        <a href="javascript:history.back()" class="text-white text-decoration-none mb-4 d-flex align-items-center">
            <i class="fa-solid fa-arrow-left me-2"></i> Quay lại
        </a>

        <nav class="nav flex-column justify-content-center flex-grow-1">
            <a class="nav-link active" href="#"><i class="fa-solid fa-music me-2"></i> Quản lý bài hát</a>
            <a class="nav-link" href="#"><i class="fa-solid fa-user me-2"></i> Quản lý người dùng</a>
            <a class="nav-link" href="#"><i class="fa-solid fa-list me-2"></i> Thể loại</a>
            <a class="nav-link" href="#"><i class="fa-solid fa-gear me-2"></i> Cài đặt</a>
        </nav>
    </aside>    

    <main class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3>Quản lí bài hát</h3>
            <button id="theme-toggle" class="btn btn-outline-secondary rounded-pill">
                <i class="fa-solid fa-circle-half-stroke"></i> Đổi chế độ
            </button>
        </div>
        <div class="admin-container p-4 d-flex flex-column align-items-center">
            <h2 class="mb-4 text-center">Công cụ quản lý bài hát</h2>
        <div class="card p-4 shadow-sm border-0 mx-auto" style="max-width: 500px; width: 100%; background-color: var(--card-bg);">
            <h5 class="mb-3" style="color: var(--text-main);">Chọn tệp nhạc để tải lên hệ thống:</h5>
    
            <form action="/admin/upload" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                
                <div class="mb-3">
                    <input type="file" name="file" class="form-control" id="fileInput" multiple required 
                        style="background-color: var(--card-bg); color: var(--text-main); border-color: var(--border-color);">
                </div>
                
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fa-solid fa-cloud-arrow-up me-2"></i>Tải lên (Upload)
                </button>
            </form>
        </div>
</div>

            <div class="mt-5">
                <h4>Danh sách bài hát</h4>
                <table class="table table-hover mt-3" id="songTable">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Tiêu đề</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Logic hiển thị dữ liệu từ Controller --%>
                        <c:choose>
                            <c:when test="${not empty songs}">
                                <c:forEach var="song" items="${songs}">
                                    <tr>
                                        <td>${song.id}</td>
                                        <td>${song.title}</td>
                                        <td><span class="badge bg-success">Đã đồng bộ</span></td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary"><i class="fa-solid fa-pen"></i></button>
                                            <button class="btn btn-sm btn-outline-danger"><i class="fa-solid fa-trash"></i></button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="text-center">Chưa có bài hát nào được tải lên.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<script>
    document.getElementById('theme-toggle').addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        
        // Chuyển đổi màu bảng giữa table-light và table-dark
        const thead = document.querySelector('#songTable thead');
        thead.classList.toggle('table-light');
        thead.classList.toggle('table-dark');
    });
</script>
</body>
</html>