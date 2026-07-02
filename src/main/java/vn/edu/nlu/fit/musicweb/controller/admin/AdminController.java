package vn.edu.nlu.fit.musicweb.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.service.MusicScannerService;

@Controller
@RequestMapping("/admin") // Ánh xạ tới /admin
public class AdminController {

    private final SongRepository songRepository;

    public AdminController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        // Lấy danh sách từ database
        model.addAttribute("songs", songRepository.findAll());
        // Trả về file JSP (đảm bảo file nằm ở /WEB-INF/views/admin.jsp)
        return "admin/admin"; 
    }
}