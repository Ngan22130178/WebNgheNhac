package vn.edu.nlu.fit.musicweb.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

@Controller
@RequestMapping("/admin/admin_manager_user")
public class AdminManagerUser {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        // Lấy danh sách từ database và gửi sang JSP
        model.addAttribute("users", userRepository.findAll());
        // Giúp menu trang quản lý người dùng được highlight (active)
        model.addAttribute("currentPage", "users"); 
        
        return "admin/admin_manager_user"; // Tên file jsp của bạn (ví dụ: webapp/WEB-INF/views/admin/user_manager.jsp)
    }
}