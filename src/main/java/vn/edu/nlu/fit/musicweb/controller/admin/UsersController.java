package vn.edu.nlu.fit.musicweb.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

@Controller
@RequestMapping("/admin/managerUsers")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        // Lấy danh sách từ database và gửi sang JSP
        model.addAttribute("users", userRepository.findAll());
        // Giúp menu trang quản lý người dùng được highlight (active)
        model.addAttribute("currentPage", "users"); 
        
        return "admin/managerUsers"; // webapp/WEB-INF/views/admin/managerUsers.jsp
    }
}