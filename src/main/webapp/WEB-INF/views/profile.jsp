<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Thư viện cá nhân | MusicWeb</title>

                <!-- Nạp Bootstrap CSS bản rút gọn CHỈ ĐỂ GIỮ HỆ THỐNG LƯỚI (GRID COL-MD-3, COL-MD-9) -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

                <script src="https://unpkg.com/htmx.org@1.9.10"></script>

                <style>
                    #mainNavbar {
                        font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif !important;
                    }

                    #mainNavbar .dropdown-toggle::after {
                        display: inline-block !important;
                        margin-left: 0.255em;
                        vertical-align: 0.255em;
                        content: "";
                        border-top: 0.3em solid;
                        border-right: 0.3em solid transparent;
                        border-bottom: 0;
                        border-left: 0.3em solid transparent;
                    }

                    #mainNavbar .d-flex.align-items-center .dropdown .dropdown-toggle::after {
                        display: none !important;
                    }

                    #mainNavbar .d-flex.align-items-center .dropdown {
                        display: flex !important;
                        align-items: center !important;
                        height: 100% !important;
                    }

                    #mainNavbar .dropdown button.dropdown-toggle {
                        margin: 0 !important;
                        display: flex !important;
                        align-items: center !important;
                        justify-content: center !important;
                    }

                    #mainNavbar .navbar-nav .nav-item {
                        margin-right: 12px !important;
                    }

                    #mainNavbar .d-flex.align-items-center {
                        display: flex !important;
                        align-items: center !important;
                        /* Giữ tất cả thành phần bên phải thẳng hàng */
                    }

                    body {
                        background-color: #f8f9fa;
                    }

                    .sidebar {
                        background-color: #ffffff;
                        min-height: calc(100vh - 56px);
                        border-right: 1px solid #e9ecef;
                        padding: 24px;
                    }

                    .sidebar-title {
                        font-size: 1.5rem;
                        font-weight: 700;
                        color: #191c1f;
                        margin-bottom: 24px;
                    }

                    .sidebar-menu .nav-link {
                        color: #495057;
                        font-weight: 500;
                        padding: 12px 16px;
                        border-radius: 8px;
                        margin-bottom: 8px;
                        transition: all 0.2s;
                    }

                    .sidebar-menu .nav-link:hover {
                        background-color: #f1f3f5;
                        color: #191c1f;
                    }

                    .sidebar-menu .nav-link.active {
                        background-color: #e9ecef;
                        color: #1db954;
                    }

                    .sidebar-menu .nav-link i {
                        width: 24px;
                        font-size: 1.1rem;
                    }

                    .content-section {
                        padding: 32px;
                    }

                    .section-title {
                        color: #1db954;
                        font-weight: 700;
                        margin-bottom: 24px;
                    }
                </style>
            </head>

            <body>
                <jsp:include page="fragments/header.jsp" />

                <div class="container-fluid">
                    <div class="row">

                        <!-- THANH SIDEBAR MENU -->
                        <div class="col-md-3 col-lg-2 sidebar">
                            <!-- <div class="sidebar-title">
                                <i class="navbar-brand fw-bold">🎵 Thư Viện</i>
                            </div> -->
                            <nav class="nav flex-column sidebar-menu" hx-on:click="
                    let currentActive = this.querySelector('.nav-link.active');
                    if(currentActive) currentActive.classList.remove('active');
                    
                    if(event.target.closest('.nav-link')) {
                        event.target.closest('.nav-link').classList.add('active');
                    }
                ">

                                <a class="nav-link" href="#" hx-get="/api/user/account" hx-target="#profileContent"
                                    style="margin-top: 15px; border-top: 1px solid #eee; padding-top: 15px;">
                                    <i class="fa-solid fa-user-gear me-2"></i>Tài khoản của tôi
                                </a>

                                <!-- Nút Playlist của tôi  -->
                                <a class="nav-link active" href="#" hx-get="/api/user/playlist/get-all"
                                    hx-target="#profileContent" hx-swap="innerHTML" hx-trigger="click, load">
                                    <i class="fa-solid fa-list-ul me-2"></i>Playlist của tôi
                                </a>

                                <!-- Nút Album đã lưu -->
                                <a class="nav-link" href="#" hx-get="/api/user/playlist/get-all"
                                    hx-target="#profileContent">
                                    <i class="fa-solid fa-record-vinyl me-2"></i>Album đã lưu
                                </a>

                                <!-- Nút Bài hát yêu thích -->
                                <a class="nav-link text-danger" href="#" hx-get="/api/user/favorite-songs"
                                    hx-target="#profileContent">
                                    <i class="fa-solid fa-heart me-2"></i>Bài hát yêu thích
                                </a>
                            </nav>
                        </div>

                        <!-- VÙNG HIỂN THỊ NỘI DUNG  -->
                        <div class="col-md-9 col-lg-10 content-section" id="profileContent">
                            <div class="text-center text-muted py-5">
                                <i class="fa-solid fa-spinner fa-spin fa-2x mb-2"></i>
                                <p>Đang tải danh sách playlist...</p>
                            </div>
                        </div>

                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

                <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        const elementsToRedirect = document.querySelectorAll(
                            "#mainNavbar .navbar-brand, #mainNavbar .nav-link:not(.dropdown-toggle), #mainNavbar .dropdown-item:not(.text-danger)"
                        );

                        elementsToRedirect.forEach(el => {
                            // Vô hiệu hóa tính năng HTMX bằng cách chặn sự kiện click
                            el.addEventListener("click", function (e) {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = "/";
                            }, true);
                        });
                    });

                    function toggleHeartUI(songId, title, artist) {
                        // Tạo ra câu thông báo đúng format: "Đã thêm: Tên bài hát - Ca sĩ"
                        const message = `Đã thêm: ${title} - ${artist}`;

                        // Gọi hàm hiển thị thông báo (Toast) có sẵn trong dự án của bạn
                        // Ví dụ dự án của bạn đang dùng một hàm tên là showToast(message) thì gọi nó ra:
                        showToast(message);

                        // Hoặc nếu bạn muốn test thử xem nó chạy đúng chưa trước khi làm giao diện đẹp:
                        // alert(message);
                    }
                </script>
            </body>

            </html>