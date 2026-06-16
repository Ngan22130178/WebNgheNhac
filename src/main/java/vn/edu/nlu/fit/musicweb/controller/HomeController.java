package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.model.Song;
import java.util.List;
import java.util.stream.Collectors;

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

    // 2. Lọc theo thể loại: Xử lý khi click vào menu trong header
    @GetMapping("/genre")
    public String filterByGenre(@RequestParam("name") String genre, Model model) {
        // SongRepository does not define findByGenre; fetch all and filter here
        List<Song> songs = songRepository.findAll();
        List<Song> filtered = songs.stream()
                .filter(s -> s.getGenre() != null && s.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
        model.addAttribute("songs", filtered);
        return "index"; // Vẫn trả về trang index nhưng dữ liệu đã bị lọc
    }
}