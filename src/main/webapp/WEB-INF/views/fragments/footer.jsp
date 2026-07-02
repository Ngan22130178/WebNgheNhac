<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- 
    FRAGMENT: footer.jsp
    Mô tả: Chân trang bao gồm thông tin bản quyền và các liên kết hỗ trợ.
    Ghi chú: Sử dụng class 'text-body-secondary' để tự động đổi màu theo Theme (Sáng/Tối).
--%>

<footer class="text-center py-4 mt-5 border-top border-secondary-subtle">
    <div class="container">
        <%-- Thông tin bản quyền --%>
        <p class="mb-1 text-body-secondary">&copy; 2026 MusicWeb - Dự án học tập NLU</p>
        
        <%-- Thông tin công nghệ --%>
        <small class="text-body-secondary">
            Phát triển với Spring Boot | HTMX | Bootstrap 5
        </small>
        
        <%-- Liên kết hỗ trợ --%>
        <div class="mt-3">
            <a href="#" class="text-decoration-none text-body-secondary mx-3 hover-link">Điều khoản</a>
            <a href="#" class="text-decoration-none text-body-secondary mx-3 hover-link">Liên hệ</a>
            <a href="#" class="text-decoration-none text-body-secondary mx-3 hover-link">Hỗ trợ</a>
        </div>
    </div>
</footer>

<style>
    /* Hiệu ứng hover cho link trong footer */
    .hover-link:hover {
        color: var(--bs-primary) !important;
        transition: color 0.2s ease-in-out;
    }
</style>