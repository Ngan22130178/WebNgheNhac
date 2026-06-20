package vn.edu.nlu.fit.musicweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Cho phép tất cả các request mà không cần kiểm tra quyền 
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Tắt CSRF hoàn toàn để không chặn H2 Console nữa
            .csrf(csrf -> csrf.disable())
            
            // Cho phép hiển thị Frame trong cùng Origin để giao diện H2 hiển thị được các ô nhập liệu
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}