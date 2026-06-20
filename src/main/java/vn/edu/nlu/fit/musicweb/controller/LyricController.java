package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import java.util.List;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongLyricsRepository;  

@RestController
@RequestMapping("/api/lyrics")
@CrossOrigin(origins = "*") 
public class LyricController {

    @Autowired
    private SongLyricsRepository songLyricsRepository;  

    @GetMapping("/{songId}")
    public ResponseEntity<String> getLyricBySongId(@PathVariable Long songId) {
        // Tìm danh sách lời bài hát dựa vào id 
        List<SongLyrics> lyrics = songLyricsRepository.findBySongId(songId);
        
        if (!lyrics.isEmpty() && lyrics.get(0).getContent() != null) {
            // Trả về CHUỖI TEXT lời bài hát 
            return ResponseEntity.ok(lyrics.get(0).getContent());
        } else {
            return ResponseEntity.ok("<span class='text-muted small italic'>Bài hát này chưa có lời cập nhật!</span>");
        }
    }
}