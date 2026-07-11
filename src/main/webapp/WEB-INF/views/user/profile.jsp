<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Thư viện cá nhân | MusicWeb</title>

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
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            background-color: #ffffff;
            min-height: calc(100vh - 56px);
            border-right: 1px solid #e9ecef;
            padding: 24px;
        }
        .sidebar-menu .nav-link {
            color: #495057;
            font-weight: 500;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 8px;
            transition: all 0.2s;
            text-decoration: none;
            cursor: pointer;
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
    </style>
</head>

<body>
    <jsp:include page="/WEB-INF/views/fragments/header.jsp" />

    <div class="container-fluid">
        <div class="row">

            <!-- THANH SIDEBAR MENU -->
            <div class="col-md-3 col-lg-2 sidebar">
                <nav class="nav flex-column sidebar-menu" hx-on:click="
                    let currentActive = this.querySelector('.nav-link.active');
                    if(currentActive) currentActive.classList.remove('active');
                    if(event.target.closest('.nav-link')) {
                        event.target.closest('.nav-link').classList.add('active');
                    }
                ">
                    <!-- Nút Tài khoản của tôi -->
                    <a class="nav-link" href="#" hx-get="/user/api/user/account" hx-target="#profileContent"
                       style="margin-top: 15px; border-top: 1px solid #eee; padding-top: 15px;">
                        <i class="fa-solid fa-user-gear me-2"></i>Tài khoản của tôi
                    </a>

                    <!-- Nút Playlist của tôi -->
                    <a class="nav-link active" href="#" hx-get="/user/api/user/playlists" hx-target="#profileContent">
                        <i class="fa-solid fa-list-ul me-2"></i>Playlist của tôi
                    </a>

                    <!-- Nút Album đã lưu -->
                    <a class="nav-link" href="#" hx-get="/user/api/user/albums" hx-target="#profileContent">
                        <i class="fa-solid fa-compact-disc me-2"></i>Album đã lưu
                    </a>

                    <!-- Nút Bài hát yêu thích -->
                    <a class="nav-link text-danger" href="#" hx-get="/user/api/user/favorite-songs" hx-target="#profileContent">
                        <i class="fa-solid fa-heart me-2"></i>Bài hát yêu thích
                    </a>
                </nav>
            </div>

            <!-- VÙNG HIỂN THỊ NỘI DUNG (ĐÃ KHẮC PHỤC HX-TRIGGER) -->
            <div class="col-md-9 col-lg-10 content-section" id="profileContent"
                 hx-get="/user/api/user/playlists"
                 hx-trigger="load" 
                 hx-swap="innerHTML">
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
                el.addEventListener("click", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    window.location.href = "/";
                }, true);
            });
        });

        // Hàm lắng nghe và tự động refresh tab hiện tại khi có thay đổi (Không lo loop vô hạn)
        document.body.addEventListener('favoriteUpdated', function() {
            const activeTab = document.querySelector('.sidebar-menu .nav-link.active');
            if (activeTab) {
                htmx.trigger(activeTab, 'click');
            }
        });

        function toggleHeartUI(songId, title, artist) {
            const message = `Đã thêm: \${title} - \${artist}`;
            if (typeof showToast === "function") {
                showToast(message);
            } else {
                alert(message);
            }
        }
    </script>

    <script>
        document.body.addEventListener('htmx:configRequest', function (evt) {
            let token = "${_csrf.token}";
            let header = "${_csrf.headerName}";
            if (token && header) {
                evt.detail.headers[header] = token;
            }
        });
    </script>
</body>
</html>