<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
            <div class="container">
                <a class="navbar-brand fw-bold" href="/" hx-get="/" hx-target="#songListBody" hx-swap="innerHTML">
                    🎵 MusicWeb
                </a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="/" hx-get="/" hx-target="#songListBody">Tất cả</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/genre" hx-get="/genre" hx-target="#songListBody">Thể loại</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/album" hx-get="/album" hx-target="#songListBody">Album</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/artist" hx-get="/artist" hx-target="#songListBody">Ca sĩ</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/favorite" hx-get="/favorite" hx-target="#songListBody">Yêu
                                thích</a>
                        </li>
                    </ul>

                    <div class="d-flex align-items-center">
                        <form class="d-flex me-3" hx-get="/search" hx-target="#songListBody">
                            <input class="form-control form-control-sm me-2" type="search" name="q"
                                placeholder="Tìm kiếm...">
                            <button class="btn btn-outline-light btn-sm" type="submit">🔍</button>
                        </form>

                        <button id="themeToggle" class="btn btn-outline-warning btn-sm me-3">🌓</button>
                        <a href="/login" class="btn btn-primary btn-sm">Đăng nhập</a>
                    </div>

                    <form class="d-flex me-3" hx-get="/search" hx-target="#songListBody">
                        <input class="form-control form-control-sm me-2" type="search" name="keyword"
                            placeholder="Tìm kiếm..." hx-get="/search" hx-trigger="keyup changed delay:300ms"
                            hx-target="#songListBody">
                        <button class="btn btn-outline-light btn-sm" type="submit">🔍</button>
                    </form>
                </div>
            </div>
        </nav>