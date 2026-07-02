package vn.edu.nlu.fit.musicweb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";
            
            // Chỉ thêm admin nếu tài khoản này chưa tồn tại
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = User.builder()
                        .email(adminEmail)
                        .password(passwordEncoder.encode("123456")) // Mã hóa mật khẩu
                        .role("ROLE_ADMIN")
                        .fullName("Administrator")
                        .provider("LOCAL")
                        .enabled(true)
                        .build();
                
                userRepository.save(admin);
                System.out.println("--- Tài khoản Admin đã được tạo thành công! ---");
            }
        };
    }
}