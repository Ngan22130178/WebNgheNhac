package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.*;
import vn.edu.nlu.fit.musicweb.repository.*;
import java.util.*;
import org.springframework.ui.Model;

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

    @GetMapping("/search")
    public String searchSongs(@RequestParam(value = "q", required = false) String keyword, Model model) {
        List<Song> searchResults;

        // Kiểm tra nếu có từ khóa thì tìm theo title
        if (keyword != null && !keyword.trim().isEmpty()) {

            // 1. Cắt khoảng trắng thừa ở 2 đầu từ khóa
            String cleanKeyword = keyword.trim();

            // Tìm kiếm theo tên bài hát
            List<Song> songsByTitle = songRepository.findByTitleContainingIgnoreCase(cleanKeyword);

            // 2. Tìm kiếm theo tên ca sĩ
            List<Song> songsByArtist = songRepository.findByArtistContainingIgnoreCase(cleanKeyword);

            // 3. Bỏ danh sách bài hát vào Set trước để giữ thứ tự và tránh trùng
            Set<Song> combinedResults = new LinkedHashSet<>(songsByTitle);

            // 4. Nạp danh sách ca sĩ vào Set   
            combinedResults.addAll(songsByArtist);

            // 5. Chuyển kết quả thành danh sách List để hiển thị
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

}
