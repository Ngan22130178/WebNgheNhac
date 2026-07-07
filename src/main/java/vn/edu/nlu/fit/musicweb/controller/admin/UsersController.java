package vn.edu.nlu.fit.musicweb.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.edu.nlu.fit.musicweb.repository.UserRepository;
import vn.edu.nlu.fit.musicweb.service.UsersService;
import vn.edu.nlu.fit.musicweb.model.User;
@Controller
@RequestMapping("/admin/managerUsers")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersService usersService;

    @GetMapping
    public String listUsers(Model model) {
        // Lấy danh sách từ database và gửi sang JSP
        model.addAttribute("users", userRepository.findAll());
        // Giúp menu trang quản lý người dùng được highlight (active)
        model.addAttribute("currentPage", "users"); 
        
        return "admin/managerUsers"; // webapp/WEB-INF/views/admin/managerUsers.jsp
    }
        // Thêm hoặc Sửa (Lưu)
    @PostMapping("/save/{id}")
    @ResponseBody 
    public String saveUser(@PathVariable Long id, @ModelAttribute User user) {
        // 1. Tìm bản ghi cũ trong DB
        User existingUser = usersService.getUserById(id);
        if (existingUser != null) {
            // 2. Cập nhật các trường từ form vào bản ghi cũ
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            existingUser.setEnabled(user.isEnabled());
            // 3. Lưu lại
            usersService.saveUser(existingUser);
            return "success";
        }
        return "error";
    }

    // Xóa
    @PostMapping("/delete/{id}")
    @ResponseBody // Trả về kết quả trực tiếp cho AJAX
    public String deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return "success"; 
    }

    // Đổi trạng thái (kích hoạt / vô hiệu hóa)
    @PostMapping("/toggleStatus/{id}")
    @ResponseBody
    public String toggleStatus(@PathVariable Long id) {
        User user = usersService.getUserById(id);
        if (user != null) {
            user.setEnabled(!user.isEnabled());
            usersService.saveUser(user);
            return "success";
        }
        return "error";
    }
}