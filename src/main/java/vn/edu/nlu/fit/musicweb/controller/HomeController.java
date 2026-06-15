package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;

@Controller
public class HomeController {

    @Autowired
    private SongRepository songRepository;

    // 1. Trang chủ: Hiển thị tất cả
    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("songs", songRepository.findAll());
        return "index";
    }

    // 2. Lọc theo thể loại: Sử dụng query của MongoDB
    @GetMapping("/genre")
    public String filterByGenre(@RequestParam("name") String genre, Model model) {
        // Gọi thẳng phương thức đã tạo ở Repository, không cần filter thủ công trong Java
        model.addAttribute("songs", songRepository.findByGenreIgnoreCase(genre));
        return "index";
    }
}