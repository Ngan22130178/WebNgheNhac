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
        try {
            List<Song> songs = songRepository.findAll();
            System.out.println("DEBUG: Số bài hát lấy được: " + (songs != null ? songs.size() : "NULL"));
            model.addAttribute("songs", songs != null ? songs : new ArrayList<>());
            return "index";
        } catch (Exception e) {
            e.printStackTrace(); // Lỗi 500 sẽ được in chi tiết ở đây
            return "error"; // Tạo file error.jsp để xem lỗi nếu cần
        }
    }
}