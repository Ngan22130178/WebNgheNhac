package vn.edu.nlu.fit.musicweb.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String displayName = "Người dùng";

            if (auth instanceof OAuth2AuthenticationToken) {
                // Đăng nhập Google: lấy tên từ thuộc tính của Google
                displayName = ((OAuth2AuthenticationToken) auth).getPrincipal().getAttribute("name");
            } else if (auth.getPrincipal() instanceof CustomUserDetails) {
                // Đăng nhập Local: lấy từ class CustomUserDetails của bạn
                displayName = ((CustomUserDetails) auth.getPrincipal()).getFullName();
            }
            
            model.addAttribute("userDisplayName", displayName);
        }
    }
}
