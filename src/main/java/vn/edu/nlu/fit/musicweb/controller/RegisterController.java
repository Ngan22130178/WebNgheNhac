package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.edu.nlu.fit.musicweb.repository.UserRepository;
import vn.edu.nlu.fit.musicweb.model.User;
import org.springframework.ui.Model;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Cần cấu hình Bean này trong SecurityConfig

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Trả về file register.jsp
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email, 
                               @RequestParam String password, 
                               @RequestParam String fullName, 
                               Model model) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email này đã được sử dụng!");
            return "register";
        }

        // Tạo user mới
        User newUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // Mã hóa mật khẩu
                .fullName(fullName)
                .role("ROLE_USER") // Mặc định là user thường
                .build();

        userRepository.save(newUser);
        return "redirect:/login";
    }

}
