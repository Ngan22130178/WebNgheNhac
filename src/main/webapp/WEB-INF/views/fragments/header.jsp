<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- ==========================================
     1. NAVIGATION BAR CONTAINER 
     ========================================== --%>
<nav class="navbar navbar-expand-lg border-bottom shadow-sm bg-body-tertiary" id="mainNavbar">
    <div class="container">
        
        <%-- Logo --%>
        <a class="navbar-brand fw-bold" href="/" hx-get="/api/songs" hx-target="#songListBody">
            🎵 MusicWeb
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            
            <%-- 2. MAIN NAVIGATION MENU --%>
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="#" hx-get="/api/songs/all" hx-target="#songListBody">Tất cả</a>
                </li>
                
                <c:set var="categories" value='<%= new String[]{"genre", "album", "artist"} %>' />
                <c:set var="labels" value='<%= new String[]{"Thể loại", "Album", "Ca sĩ"} %>' />
                
                <c:forEach var="i" begin="0" end="2">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown"
                           hx-get="/api/songs/categories/${categories[i]}" 
                           hx-target="#${categories[i]}-menu" 
                           hx-trigger="mouseenter once"
                           hx-indicator="#globalIndicator">
                           ${labels[i]}
                        </a>
                        <ul class="dropdown-menu" id="${categories[i]}-menu">
                            <li><a class="dropdown-item">Đang tải...</a></li>
                        </ul>
                    </li>
                </c:forEach>
            </ul>

            <%-- 3. SEARCH & USER ACTIONS --%>
            <div class="d-flex align-items-center">
                
                <%-- Live Search với Delay --%>
                <form class="d-flex me-3" hx-get="/api/songs/search" hx-target="#songListBody">
                    <input class="form-control form-control-sm me-2" type="search" name="q" 
                           placeholder="Tìm kiếm..."
                           hx-get="/api/songs/search" 
                           hx-trigger="keyup changed delay:500ms" 
                           hx-target="#songListBody"
                           hx-indicator="#globalIndicator">
                </form>

                <%-- Loading Indicator (Ẩn mặc định) --%>
                <span class="htmx-indicator spinner-border spinner-border-sm text-primary me-2" id="globalIndicator"></span>
                
                <button id="themeToggle" class="btn btn-outline-secondary btn-sm me-2" onclick="toggleTheme()">🌓</button>

                <%-- Session Logic: Hiển thị User hoặc Nút Đăng nhập --%>
                <sec:authorize access="!isAuthenticated()">
                    <a href="/login" class="btn btn-primary btn-sm">Đăng nhập</a>
                </sec:authorize>

                <sec:authorize access="isAuthenticated()">
                    <div class="dropdown">
                        <%-- Lấy fullName từ principal (đã cấu hình trong CustomUserDetails) --%>
                        <sec:authentication property="principal.fullName" var="fullName" />
                        
                        <%-- Logic tạo avatar chữ cái --%>
                        <c:set var="nameParts" value="${fn:split(fullName, ' ')}" />
                        <c:set var="lastName" value="${nameParts[fn:length(nameParts)-1]}" />
                        <c:set var="firstChar" value="${fn:substring(lastName, 0, 1)}" />
                        
                        <%-- Màu sắc ngẫu nhiên dựa trên độ dài tên --%>
                        <c:set var="colors" value='<%= new String[]{"#e57373", "#f06292", "#ba68c8", "#9575cd", "#7986cb", "#64b5f6", "#4fc3f7"} %>' />
                        <c:set var="colorIndex" value="${fn:length(fullName) % 7}" />

                        <button class="btn btn-sm p-0 rounded-circle border-0 dropdown-toggle" data-bs-toggle="dropdown">
                            <div class="d-flex align-items-center justify-content-center rounded-circle" 
                                style="width: 32px; height: 32px; background-color: #007bff; color: white; font-weight: bold;">
                                ${fn:toUpperCase(firstChar)}
                            </div>
                        </button>

                        <ul class="dropdown-menu dropdown-menu-end">
                            <sec:authorize access="isAuthenticated()">
                                <li class="px-3 py-1 fw-bold text-muted small">Xin chào, ${userDisplayName}</li>
                            </sec:authorize>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="/user">Tài khoản</a></li>
                            <li>
                                <form action="/logout" method="POST">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button type="submit" class="dropdown-item text-danger">Đăng xuất</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </div>
</nav>

<%-- ==========================================
     4. GLOBAL STYLES (HTMX Indicator)
     ========================================== --%>
<style>
    .htmx-indicator { opacity: 0; transition: opacity 200ms; }
    .htmx-request .htmx-indicator { opacity: 1; }

    /* Ẩn mũi tên dropdown */
    .dropdown-toggle::after {
        display: none;
    }
    
    /* Hiệu ứng di chuột vào avatar cho đẹp hơn */
    .avatar-hover:hover {
        opacity: 0.8;
        cursor: pointer;
    }
    
    /* ... (giữ nguyên style htmx-indicator cũ) ... */
</style>