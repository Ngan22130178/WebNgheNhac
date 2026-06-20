<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="dropdown">
                            <button class="btn btn-sm btn-outline-primary dropdown-toggle" data-bs-toggle="dropdown">
                                ${sessionScope.user.username}
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="/profile">Tài khoản</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="/logout">Đăng xuất</a></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="/login" class="btn btn-primary btn-sm">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
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
</style>
