package vn.edu.nlu.fit.musicweb.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// @Component: Đánh dấu đây là một Bean để Spring tự động quản lý và inject vào SecurityConfig
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Phương thức này được tự động gọi khi người dùng đăng nhập thành công.
     * * @param request: Yêu cầu từ phía người dùng
     * @param response: Phản hồi gửi trả lại trình duyệt
     * @param authentication: Chứa thông tin người dùng đã đăng nhập (username, roles,...)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        
        // 1. Lấy danh sách quyền (Authorities) của user vừa đăng nhập
        // Ví dụ: "[ROLE_ADMIN]" hoặc "[ROLE_USER]"
        String role = authentication.getAuthorities().toString();
        
        // 2. Logic điều hướng dựa trên Role
        // Kiểm tra nếu role chứa chuỗi "ROLE_ADMIN" thì đẩy về trang Quản trị
        if (role.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } 
        // Ngược lại, tất cả các user thường sẽ được đẩy về trang Cá nhân
        else {
            response.sendRedirect("/user/profile");
        }
    }
}