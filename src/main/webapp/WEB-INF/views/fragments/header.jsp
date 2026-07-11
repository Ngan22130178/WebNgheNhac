<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
    .scrollable-menu {
        max-height: 275px; /* 7 dòng */
        overflow-y: auto; /* Tự động hiển thị thanh cuộn khi vượt quá độ cao này */
        overflow-x: hidden; /* Ngăn nội dung bị tràn ra ngoài theo chiều ngang */
        padding: 0.5rem 0; /* Đảm bảo menu không bị dính vào viền trình duyệt */
    }
</style>

<%--========================================== 1. NAVIGATION BAR CONTAINER ==========================================--%>
<nav class="navbar navbar-expand-lg border-bottom shadow-sm bg-body-tertiary sticky-top" id="mainNavbar">
    <div class="container">
        <!-- VỊ TRÍ 1: Logo (Thêm hx-select) -->
        <a class="navbar-brand fw-bold" href="#" hx-get="/api/songs/all" hx-target="#songListBody" hx-select="#dynamicSongRows">
            🎵 MusicWeb
        </a>

        <!-- Hamburger Menu Button for Responsive Design -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <%-- 2. MAIN NAVIGATION MENU --%>
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <!-- VỊ TRÍ 2: Nút Tất cả (Thêm hx-select) -->
                    <a class="nav-link" href="#" hx-get="/api/songs/all" hx-target="#songListBody" hx-select="#dynamicSongRows">Tất cả</a>
                </li>

                <c:set var="categories" value='<%= new String[]{"genre", "album", "artist"} %>' />
                <c:set var="labels" value='<%= new String[]{"Thể loại", "Album", "Ca sĩ"} %>' />

                <c:forEach var="i" begin="0" end="2">
                    <li class="nav-item dropdown">
                        <!-- Lưu ý: Menu này tải danh mục (thể loại/ca sĩ), khi người dùng click chọn 1 thể loại cụ thể 
                             thì logic xử lý nằm ở trong Controller trả về của menu đó. Bạn không cần thêm hx-select tại đây -->
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown"
                           hx-get="/api/songs/categories/${categories[i]}"
                           hx-target="#${categories[i]}-menu" 
                           hx-trigger="mouseenter once"
                           hx-indicator="#globalIndicator">
                             ${labels[i]}
                        </a>
                        <ul class="dropdown-menu scrollable-menu" id="${categories[i]}-menu">
                            <li><a class="dropdown-item">Đang tải...</a></li>
                        </ul>
                    </li>
                </c:forEach>
            </ul>

            <%-- 3. SEARCH & USER ACTIONS --%>
            <div class="d-flex align-items-center">
                <!-- Live Search with Delay -->
                <!-- VỊ TRÍ 3: Ô tìm kiếm (Thêm hx-select ở cả thẻ form và thẻ input bên dưới để đồng bộ hoàn toàn) -->
                <form class="d-flex me-3" hx-get="/api/songs/search" hx-target="#songListBody" hx-select="#dynamicSongRows">
                    <input class="form-control form-control-sm me-2" type="search" name="q" placeholder="Tìm kiếm..."
                           hx-get="/api/songs/search" 
                           hx-trigger="keyup changed delay:500ms"
                           hx-target="#songListBody" 
                           hx-select="#dynamicSongRows"
                           hx-indicator="#globalIndicator">
                </form>

                <!-- Loading Indicator (Hidden by default) -->
                <span class="htmx-indicator spinner-border spinner-border-sm text-primary me-2" id="globalIndicator"></span>

                <button id="themeToggle" class="btn btn-outline-secondary btn-sm me-2" onclick="toggleTheme()">🌓</button>

                <!-- Session Logic: Show User Profile or Login Button -->
                <sec:authorize access="!isAuthenticated()">
                    <a href="/login" class="btn btn-primary btn-sm">Đăng nhập</a>
                </sec:authorize>

                <sec:authorize access="isAuthenticated()">
                    <div class="dropdown">
                        <!-- Get fullName from Spring Security Principal -->
                        <sec:authentication property="principal.fullName" var="fullName" />

                        <!-- Logic to generate a text avatar from the first character of the full name -->
                        <c:set var="firstChar" value="${fn:substring(fullName, 0, 1)}" />

                        <!-- Random color from an array based on the length of the name to create a diverse experience -->
                        <c:set var="colors" value='<%= new String[]{"#e57373", "#f06292", "#ba68c8", "#9575cd", "#7986cb", "#64b5f6", "#4fc3f7"} %>' />
                        <c:set var="colorIndex" value="${fn:length(fullName) % 7}" />

                        <button class="btn btn-sm p-0 rounded-circle border-0 dropdown-toggle avatar-hover" data-bs-toggle="dropdown">
                            <!-- text avatar fallback -->
                            <div class="d-flex align-items-center justify-content-center rounded-circle"
                                 style="width: 32px; height: 32px; background-color: #007bff; color: white; font-weight: bold;">
                                ${fn:toUpperCase(firstChar)}
                            </div>
                        </button>

                        <ul class="dropdown-menu dropdown-menu-end shadow border-0" style="min-width: 200px;">
                            <li class="px-3 py-2 fw-bold text-muted small border-bottom">
                                <i class="fa-solid fa-user-circle me-1"></i>
                                ${userDisplayName}
                            </li>
                            <li>
<<<<<<< Updated upstream
                                <a class="dropdown-item" href="/profile">
                                    <i class="fa-solid fa-user-gear me-2"></i> Cá nhân
=======
                                <a class="dropdown-item" href="/user/profile">
                                    <i class="fa-solid fa-user-gear me-2"></i> Tài khoản của tôi
>>>>>>> Stashed changes
                                </a>
                            </li>
                            <sec:authorize access="hasRole('ADMIN')">
                                <li>
                                    <a class="dropdown-item text-primary fw-bold" href="/admin/songs">
                                        <i class="fa-solid fa-shield-halved me-2"></i> Trang quản trị
                                    </a>
                                </li>
                            </sec:authorize>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <form action="/logout" method="POST" class="m-0">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button type="submit" class="dropdown-item text-danger">
                                        <i class="fa-solid fa-right-from-bracket me-2"></i> Đăng xuất
                                    </button>
                                </form>
                            </li>
                        </ul>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </div>
</nav>

<%--========================================== 4. GLOBAL STYLES (HTMX Indicator) ==========================================--%>
<style>
    .htmx-indicator {
        opacity: 0;
        transition: opacity 200ms;
    }
    .htmx-request .htmx-indicator {
        opacity: 1;
    }
    /* Hide default dropdown arrow for cleaner look */
    .dropdown-toggle::after {
        display: none;
    }
    /* Hover effect for avatar */
    .avatar-hover:hover {
        opacity: 0.8;
        cursor: pointer;
    }
    .navbar {
        position: sticky;
        top: 0;
        z-index: 1020;
        background-color: var(--bs-body-bg) !important;
    }
</style>
<<<<<<< Updated upstream

<script src="js/player-ui.js"></script>

<%-- Chèn đoạn này vào dưới cùng file header.jsp --%>
=======
<script src="js/player-ui.js"></script>

>>>>>>> Stashed changes
<script>
    function toggleHeartUI(songId, title, artist) {
        // 1. Tạo nội dung câu thông báo đúng định dạng: "Đã thêm: Tên bài hát - Ca sĩ"
        const message = `Đã thêm vào bài hát yêu thích`;

        // 2. Tự động tạo một thẻ div thông báo động
        const toast = document.createElement("div");
        toast.innerText = message;

        // 3. Sử dụng class Bootstrap để làm hộp trắng bo góc, đổ bóng xịn sò
        toast.className = "position-fixed start-50 translate-middle-x bg-white text-dark px-4 py-2 rounded-3 shadow-lg border";
        
        // Căn chỉnh vị trí xuất hiện ở giữa phía trên (ngay dưới navbar một chút)
        toast.style.top = "40px"; 
        toast.style.zIndex = "10000"; // Đảm bảo nổi lên trên tất cả thành phần khác
        toast.style.fontSize = "0.95rem";
        toast.style.fontWeight = "500";
        toast.style.animation = "fadeInOutToast 3s forwards"; // Hiệu ứng mượt mà

        // 4. Tạo nhanh hiệu ứng CSS Animation động cho Toast
        if (!document.getElementById("toast-animation-style")) {
            const style = document.createElement("style");
            style.id = "toast-animation-style";
            style.innerHTML = `
                @keyframes fadeInOutToast {
                    0% { opacity: 0; transform: translate(-50%, -20px); }
                    15% { opacity: 1; transform: translate(-50%, 0); }
                    85% { opacity: 1; transform: translate(-50%, 0); }
                    100% { opacity: 0; transform: translate(-50%, -20px); }
                }
            `;
            document.head.appendChild(style);
        }

        // 5. Đẩy thông báo ra màn hình hiển thị
        document.body.appendChild(toast);

        // 6. Sau 3 giây tự động biến mất hoàn toàn và xóa khỏi bộ nhớ trình duyệt
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
</script>