package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1. Lấy thông tin thô từ Google
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String email = oAuth2User.getAttribute("email");
        
        // 2. Logic tìm/tạo user trong DB (giống code cũ)
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .fullName(oAuth2User.getAttribute("name"))
                    .role("ROLE_USER") // Gán role từ DB
                    .provider("GOOGLE")
                    .build();
            return userRepository.save(newUser);
        });

        // 3. ĐÂY LÀ CHỖ QUAN TRỌNG: 
        // Tạo Authorities từ role trong DB để Spring Security hiểu được quyền
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

        // 4. Trả về DefaultOAuth2User đã được "nâng cấp" với Authorities
        return new DefaultOAuth2User(
            authorities,              // Danh sách quyền (ROLE_USER)
            oAuth2User.getAttributes(), // Toàn bộ data từ Google
            "email"                   // Attribute dùng làm key định danh
        );
    }
}
