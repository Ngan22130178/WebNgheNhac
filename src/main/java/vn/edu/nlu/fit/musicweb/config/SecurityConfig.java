package vn.edu.nlu.fit.musicweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.edu.nlu.fit.musicweb.service.CustomOAuth2UserService;
import vn.edu.nlu.fit.musicweb.service.CustomUserDetailsService;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    // --- 1. CẤU HÌNH MÃ HÓA & XÁC THỰC (CORE) ---
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Dùng BCrypt để băm mật khẩu
    }

  @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Truyền trực tiếp userDetailsService vào constructor
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        
        // Set PasswordEncoder như cũ
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    // --- 2. CẤU HÌNH PHÂN QUYỀN (HIERARCHY) ---
    
    @Bean
    public RoleHierarchy roleHierarchy() {
        // ADMIN > USER: ADMIN tự động có quyền của USER
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
    }

    // --- 3. CẤU HÌNH CHUỖI LỌC BẢO MẬT (FILTER CHAIN) ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // 1. Cho phép tất cả mọi người (kể cả khách) truy cập các trang công cộng
                .requestMatchers("/", "/index", "/login", "/oauth2/**", "/register", 
                                "/WEB-INF/views/**", "/audio/**", "/css/**", "/images/**", "/js/**", "/api/**").permitAll()
                
                // 2. Chỉ Admin mới được vào /admin/**
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 3. Admin và User đều vào được /user/**
                // Lưu ý: hasAnyRole tự động thêm tiền tố ROLE_, nên ở đây dùng "ADMIN", "USER"
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                
                // 4. Các yêu cầu còn lại phải đăng nhập (bất kể vai trò gì)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email") // Trùng với name="email" trong input JSP
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(info -> info.userService(customOAuth2UserService))
                .defaultSuccessUrl("/", true)
            );
        return http.build();
    }
}
