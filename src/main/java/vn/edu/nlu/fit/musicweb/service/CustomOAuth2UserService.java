package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String email = oAuth2User.getAttribute("email");
        String googleId = oAuth2User.getAttribute("sub"); // Lấy ID của Google
        
        // 1. Tìm user theo googleId trước
        User user = userRepository.findByGoogleId(googleId).orElseGet(() -> {
            // 2. Nếu không có googleId, tìm theo email để liên kết
            return userRepository.findByEmail(email).map(existingUser -> {
                // Liên kết GoogleId vào tài khoản email đã có
                existingUser.setGoogleId(googleId);
                existingUser.setProvider("GOOGLE");
                return userRepository.save(existingUser);
            }).orElseGet(() -> {
                // 3. Nếu chưa có, tạo mới hoàn toàn
                User newUser = User.builder()
                        .email(email)
                        .fullName(oAuth2User.getAttribute("name"))
                        .avatarUrl(oAuth2User.getAttribute("picture"))
                        .googleId(googleId)
                        .provider("GOOGLE")
                        .role("ROLE_USER")
                        .enabled(true)
                        .build();
                return userRepository.save(newUser);
            });
        });

        // Tạo Authorities
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

        // Trả về đối tượng OAuth2User đã được định danh chính xác
        return new DefaultOAuth2User(
            authorities,
            oAuth2User.getAttributes(),
            "email"
        );
    }
}
