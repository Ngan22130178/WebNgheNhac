package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.*;
import vn.edu.nlu.fit.musicweb.repository.*;
import java.util.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class SongController {

    @Autowired
    private SongLyricsRepository songLyricsRepository;

    @Autowired // tự động kết nối SongRepository vào controller
    private SongRepository songRepository;

    @GetMapping("/api/songs/{id}/lyrics")
    @ResponseBody
    public List<SongLyrics> getLyricsBySong(@PathVariable Long id) {
        return songLyricsRepository.findBySongId(id);
    }
    @GetMapping("/song/lyrics/{id}")
    public String showSongLyrics(@PathVariable Long id, Model model) {
        // Lấy danh sách lời bài hát từ repository bằng id bài hát
        List<SongLyrics> lyricsList = songLyricsRepository.findBySongId(id);
        
        // Nếu tìm thấy lời bài hát, lấy phần tử đầu tiên ra để hiển thị
        if (lyricsList != null && !lyricsList.isEmpty()) {
            model.addAttribute("lyrics", lyricsList.get(0));
        } else {
            model.addAttribute("lyrics", null);
        }
        
        // Trả về file giao diện chứa lời bài hát  
        return "fragments/lyric_display";
    }

    @GetMapping("/search")
    public String searchSongs(@RequestParam(value = "q", required = false) String keyword, Model model) {
        List<Song> searchResults;

        // Kiểm tra nếu có từ khóa thì tìm theo title
        if (keyword != null && !keyword.trim().isEmpty()) {

            String cleanKeyword = keyword.trim();

            // Tìm kiếm theo tên bài hát
            List<Song> songsByTitle = songRepository.findByTitleContainingIgnoreCase(cleanKeyword);

            // Tìm kiếm theo tên ca sĩ
            List<Song> songsByArtist = songRepository.findByArtistContainingIgnoreCase(cleanKeyword);

            // Bỏ danh sách bài hát vào Set trước để giữ thứ tự và tránh trùng
            Set<Song> combinedResults = new LinkedHashSet<>(songsByTitle);

            // Nạp danh sách ca sĩ vào Set   
            combinedResults.addAll(songsByArtist);

            // Chuyển kết quả thành danh sách List để hiển thị
            searchResults = new ArrayList<>(combinedResults);
        }
        // Nếu không có từ khóa, trả về danh sách rỗng
        else {
            searchResults = new ArrayList<>();
        }

        // Đẩy danh sách bài hát
        model.addAttribute("songs", searchResults);

        // Đẩy ngược lại từ khóa để hiển thị trong ô tìm kiếm
        model.addAttribute("keyword", keyword);

        return "fragments/songs_table";
    }

    @PostMapping("/api/queue/add")
    @ResponseBody
    public String addToQueue(@RequestParam("songId") Long songId, HttpSession session) {
        // Lấy danh sách hàng đợi từ Session
        List<Long> playQueue = (List<Long>) session.getAttribute("PLAY_QUEUE");
        
        // Nếu Session chưa có hàng đợi thì khởi tạo mới
        if (playQueue == null) {
            playQueue = new ArrayList<>();
        }
        
        // Thêm bài hát vào danh sách 
        if (!playQueue.contains(songId)) {
            playQueue.add(songId);
        }
        
        // Cập nhật lại danh sách vào Session
        session.setAttribute("PLAY_QUEUE", playQueue);
        
        // Trả về một đoạn text ẩn hoặc script thông báo nhỏ cho HTMX 
        return "<script>console.log('Đã thêm bài hát ID " + songId + " vào hàng đợi. Tổng số bài: " + playQueue.size() + "');</script>";
    }

}
