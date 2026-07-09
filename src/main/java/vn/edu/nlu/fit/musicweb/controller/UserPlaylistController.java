package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/user/playlist")
public class UserPlaylistController {

    @Autowired 
    private SongRepository songRepository;

    // ========================================================================
    // 1. XỬ LÝ LƯU: Khi bấm "+ Thêm", tìm đúng bài đó lưu vào danh sách của User
    // ========================================================================
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addSongToPlaylist(@RequestParam("songId") Long songId, 
                                                    Authentication authentication,
                                                    HttpSession session) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Bạn cần đăng nhập!");
        }

        try {
            // Lấy danh sách bài hát riêng của user này ra từ session (nếu chưa có thì tạo mới)
            String sessionKey = "playlist_" + authentication.getName();
            List<Song> userPlaylist = (List<Song>) session.getAttribute(sessionKey);
            if (userPlaylist == null) {
                userPlaylist = new ArrayList<>();
            }

            // Tìm chính xác bài hát dựa vào ID mà bạn vừa click
            Song song = songRepository.findById(songId).orElse(null);
            
            if (song != null) {
                // Kiểm tra xem bài hát đã có trong danh sách chưa, nếu chưa thì mới thêm vào để tránh trùng lặp
                boolean exits = userPlaylist.stream().anyMatch(s -> s.getId().equals(songId));
                if (!exits) {
                    userPlaylist.add(song);
                }
            }

            // Lưu ngược lại danh sách vào bộ nhớ session
            session.setAttribute(sessionKey, userPlaylist);
            return ResponseEntity.ok("Success");
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Lỗi: " + e.getMessage());
        }
    }

    // ========================================================================
    // 2. XỬ LÝ HIỂN THỊ: Chỉ lấy ra đúng những bài đã được lưu ở trên
    // ========================================================================
    @GetMapping("/get-all")
    public String getUserPlaylist(Authentication authentication, HttpSession session, Model model) {
        List<Song> mySongs = new ArrayList<>();

        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy danh sách bài hát theo tên User đang đăng nhập
            String sessionKey = "playlist_" + authentication.getName();
            List<Song> userPlaylist = (List<Song>) session.getAttribute(sessionKey);
            if (userPlaylist != null) {
                mySongs = userPlaylist;
            }
        }

        // Đẩy đúng danh sách bài hát được chọn ra giao diện
        model.addAttribute("songs", mySongs);
        return "profile_playlist";
    }
}