package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.repository.UserRepository; // 1. Thêm import UserRepository vào đây
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;
import java.security.Principal;
import java.util.ArrayList;

@Controller
public class HomeController {
    @Autowired private SongRepository songRepository;
    @Autowired private UserRepository userRepository; // 2. Tiêm UserRepository vào đây để gọi hàm lấy nhạc yêu thích

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        // 1. Lấy tất cả dữ liệu bài hát hiển thị ra trang chủ
        List<Song> songs = songRepository.findAll();
        model.addAttribute("songs", (songs != null) ? songs : new ArrayList<>());
        
        // 2. Xử lý danh sách ID bài hát đã thích (nếu user đã đăng nhập)
        List<Long> favoriteSongIds = new ArrayList<>();
        if (principal != null) {
            String username = principal.getName(); // Lấy email/username của người dùng hiện tại
            
            // 3. THAY ĐỔI Ở ĐÂY: Gọi qua userRepository thay vì songRepository
            List<Song> favSongs = userRepository.findFavoriteSongsByUsername(username);
            if (favSongs != null) {
                // Gom tất cả ID bài hát đã thích lại thành một danh sách
                favoriteSongIds = favSongs.stream().map(Song::getId).toList();
            }
        }
        
        // 3. Đẩy danh sách ID bài hát đã thích sang giao diện HTML
        model.addAttribute("favoriteSongIds", favoriteSongIds);
        
        return "index";
    }
}