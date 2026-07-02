package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;
import java.util.ArrayList;

@Controller
public class HomeController {
    @Autowired private SongRepository songRepository;

    @GetMapping("/")
    public String index(Model model) {
        // Chỉ tập trung lấy dữ liệu bài hát
        List<Song> songs = songRepository.findAll();
        model.addAttribute("songs", (songs != null) ? songs : new ArrayList<>());
        
        // Không cần truyền thêm username vào model nữa
        return "index";
    }
}
