<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>MusicWeb Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
        <a href="${pageContext.request.contextPath}/" class="text-white text-decoration-none mb-4 d-flex align-items-center">
            <i class="fa-solid fa-arrow-left me-2"></i> Quay lại
        </a>

        <nav class="nav flex-column justify-content-center flex-grow-1">
            <a class="nav-link ${currentPage == 'songs' ? 'active' : ''}" href="/admin/songs">
                <i class="fa-solid fa-music me-2"></i> Quản lý bài hát
            </a>
            <a class="nav-link ${currentPage == 'users' ? 'active' : ''}" href="/admin/managerUsers">
                <i class="fa-solid fa-user me-2"></i> Quản lý người dùng
            </a>
            <a class="nav-link ${currentPage == 'convert' ? 'active' : ''}" href="/admin/convert">
                <i class="fa-solid fa-file-video me-2"></i> 
                <span>Video to MP3</span>
            </a>
            <a class="nav-link ${currentPage == 'settings' ? 'active' : ''}" href="/admin/settings">
                <i class="fa-solid fa-gear me-2"></i> Cài đặt
            </a>
        </nav>
    </aside>    

    <main class="flex-grow-1 p-4" style="min-height: 100vh;">
    <div class="d-flex justify-content-end mb-4">
        <button id="theme-toggle" class="btn btn-outline-secondary rounded-pill">
            <i class="fa-solid fa-circle-half-stroke"></i> Đổi chế độ
        </button>
    </div>

    <div class="d-flex justify-content-center align-items-center" style="min-height: 70vh;">
        <div class="card shadow-sm p-5" style="width: 100%; max-width: 500px;">
            <h3 class="text-center mb-4">Convert Video to MP3</h3>
            
            <form action="${pageContext.request.contextPath}/admin/convert/process" 
                  method="POST" 
                  enctype="multipart/form-data">
                
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                
                <div class="mb-4">
                    <label class="form-label fw-bold">Chọn các file Video (.mp4, .mov, .avi):</label>
                    <input type="file" name="files" class="form-control" multiple accept="video/*" required>
                    <small class="text-muted">Có thể chọn nhiều file cùng lúc.</small>
                </div>
                
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-lg">
                        <i class="fa-solid fa-cloud-arrow-up me-2"></i> Chuyển thành MP3
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>
</div>
<div id="toastNotification" style="
    position: fixed; 
    top: 20px; 
    left: 50%; 
    transform: translateX(-50%); /* Căn giữa hoàn hảo */
    padding: 15px 30px; 
    background-color: #28a745; 
    color: white; 
    border-radius: 50px; /* Bo tròn góc kiểu hiện đại */
    display: none; 
    z-index: 9999; 
    box-shadow: 0 4px 15px rgba(0,0,0,0.2);">
    Cập nhật thành công!
</div>
<script>
    document.getElementById('theme-toggle').addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        
        // Chuyển đổi màu bảng giữa table-light và table-dark
        const thead = document.querySelector('#songTable thead');
        thead.classList.toggle('table-light');
        thead.classList.toggle('table-dark');
    });

    // Hàm hiển thị thông báo
    function showToast(message, color = "#28a745") {
        const toast = document.getElementById('toastNotification');
        toast.innerText = message;
        toast.style.backgroundColor = color;
        toast.style.display = 'block';

        // Tự động ẩn sau 2.5 giây
        setTimeout(() => {
            toast.style.display = 'none';
        }, 2500);
    }

    // 
    document.querySelector('form').addEventListener('submit', async function(e) {
        e.preventDefault(); // Chặn reload trang
        
        const formData = new FormData(this);
        const toast = document.getElementById('toastNotification');
        
        try {
            const response = await fetch(this.action, {
                method: 'POST',
                body: formData
            });
            
            const result = await response.json();
            
            if (response.ok) {
                showToast(result.message, "#28a745"); // Màu xanh lá
            } else {
                showToast(result.message, "#dc3545"); // Màu đỏ cho lỗi
            }
        } catch (error) {
            showToast("Có lỗi xảy ra khi kết nối server", "#dc3545");
        }
    });
    </script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script> 
</body>
</html>