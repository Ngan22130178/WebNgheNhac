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
    public String searchSongs(@RequestParam(value="q", required=false) String keyword, Model model){
        List<Song> searchResults;

        // Kiểm tra nếu có từ khóa thì tìm theo title 
        if (keyword != null && !keyword.trim().isEmpty()){
            searchResults = songRepository.findByTitleContainingIgnoreCase(keyword);
        } 
        // Nếu không có từ khóa, trả về danh sách rỗng
        else{
            searchResults = new ArrayList<>(); 
        }

        // Đẩy danh sách bài hát 
        model.addAttribute("songs", searchResults);

        // Đẩy ngược lại từ khóa để hiển thị trong ô tìm kiếm
        model.addAttribute("keyword", keyword);

        return "fragments/songs_table"; 
    }

}
