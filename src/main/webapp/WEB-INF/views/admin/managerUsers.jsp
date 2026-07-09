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

    <main class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3>Quản lí người dùng</h3>
            <button id="theme-toggle" class="btn btn-outline-secondary rounded-pill">
                <i class="fa-solid fa-circle-half-stroke"></i> Đổi chế độ
            </button>
        </div>
        <div class="admin-container p-4 d-flex flex-column align-items-center">
                <h4>Danh sách người dùng</h4>
                <table class="table table-hover mt-3" id="songTable">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Tên</th>
                            <th>Email</th>
                            <th>Trạng thái</th>
                            <th>Vai trò</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Logic hiển thị dữ liệu từ Controller --%>
                        <c:choose>
                            <c:when test="${not empty users}">
                                <c:forEach var="user" items="${users}">
                                    <tr id="row-user-${user.id}">
                                        <td>${user.id}</td>
                                        <td>${user.fullName}</td>
                                        <td>${user.email}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.enabled}">
                                                    <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">
                                                        <i class="fa-solid fa-check-circle me-1"></i> Hoạt động
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill">
                                                        <i class="fa-solid fa-times-circle me-1"></i> Không hoạt động
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${user.role}</td>
                                        <td>
                                            <c:choose>
                                                <%-- Nếu tài khoản đang mở (enabled = true) -> Hiện nút Khóa --%>
                                                <c:when test="${user.enabled}">
                                                    <button type="button" class="btn btn-sm btn-outline-warning" 
                                                            onclick="toggleLock('${user.id}', false)" title="Khóa tài khoản">
                                                        <i class="fa-solid fa-lock"></i>
                                                    </button>
                                                </c:when>
                                                
                                                <%-- Nếu tài khoản đang khóa (enabled = false) -> Hiện nút Mở khóa --%>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-sm btn-outline-success" 
                                                            onclick="toggleLock('${user.id}', true)" title="Mở khóa tài khoản">
                                                        <i class="fa-solid fa-unlock"></i>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                           <button type="button" class="btn btn-sm btn-outline-primary" 
                                                    data-bs-toggle="modal" data-bs-target="#editUserModal${user.id}">
                                                <i class="fa-solid fa-pen"></i>
                                            </button>                                     
                                            <button type="button" class="btn btn-sm btn-outline-danger" 
                                                onclick="confirmDelete('${user.id}', '${user.fullName}')">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                            
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:forEach var="user" items="${users}">
                                    <c:set var="user" value="${user}" scope="request" />
                                    <jsp:include page="editUserModal.jsp" />
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="text-center">Chưa có người dùng nào.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
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

    function toggleLock(id, enable) {
        const action = enable ? "Mở khóa" : "Khóa";
        
        if (confirm('Bạn có chắc chắn muốn ' + action + ' tài khoản này không?')) {
            // Sử dụng fetch API để gọi tới Backend
            fetch('/admin/managerUsers/toggleStatus/' + id, {
                method: 'POST'
            })
            .then(response => {
                if (response.ok) {
                    location.reload(); // Tải lại trang để cập nhật icon
                } else {
                    alert("Có lỗi xảy ra khi thực hiện.");
                }
            });
        }
    }

    function confirmDelete(id, title) {
        if (!confirm('Bạn có chắc chắn muốn xóa người dùng: ' + title + '?')) {
            return; 
        }
        
        fetch('/admin/managerUsers/delete/' + id, {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                // 1. Xóa dòng tương ứng khỏi bảng HTML
                const row = document.getElementById('row-user-' + id);
                if (row) {
                    row.remove();
                }
                
                // 2. Hiện thông báo cho người dùng
                alert('Đã xóa người dùng: ' + title);
            } else {
                alert('Có lỗi xảy ra!');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function submitEditForm(event, formElement) {
        event.preventDefault(); 
        
        const formData = new FormData(formElement);
        const id = formData.get('id');

        fetch('/admin/managerUsers/save/' + id, {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            if (data === "success") {
                // Hiển thị thông báo thay vì alert
                showToast("Cập nhật thành công!");
                
                updateRowInTable(id, formData);
                const modalEl = document.getElementById('editUserModal' + id);
                const modal = bootstrap.Modal.getInstance(modalEl);
                modal.hide();
            } else {
                showToast("Có lỗi xảy ra!", "#dc3545"); // Màu đỏ cho lỗi
            }
        })
        .catch(error => console.error('Error:', error));
    }

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

    function updateRowInTable(id, formData) {
        // 1. Tìm thẻ <tr> có id="row-user-${user.id}"
        const row = document.getElementById('row-user-' + id);
        
        if (row) {
            // 2. Cập nhật các cột dựa trên chỉ số (cells[0] là cột đầu tiên)
            // Hãy điều chỉnh chỉ số [x] cho khớp với bảng của bạn
            row.cells[1].innerText = formData.get('fullName');    // Cột Tên người dùng
            row.cells[2].innerText = formData.get('email');
            row.cells[3].innerText = formData.get('enabled') === 'true' ? 'Hoạt động' : 'Không hoạt động';    // Cột Trạng thái
            row.cells[4].innerText = formData.get('role');   
                 // Cột Role

            // Tạo hiệu ứng nhấp nháy nhẹ để người dùng biết dòng đó vừa được sửa
            row.style.backgroundColor = "#fff3cd"; // Màu vàng nhạt
            setTimeout(() => { row.style.backgroundColor = ""; }, 1000); // Trở về màu cũ sau 1 giây
        }
    }
    </script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>