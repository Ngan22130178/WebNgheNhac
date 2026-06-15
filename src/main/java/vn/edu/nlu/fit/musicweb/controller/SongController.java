package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;

import java.util.Collections;
import java.util.List;

@RestController // Dùng @RestController thay vì @Controller + @ResponseBody cho API
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/api/songs/{id}/lyrics")
    public List<SongLyrics> getLyricsBySong(@PathVariable String id) {
        // Truy xuất trực tiếp từ document Song
        return songRepository.findById(id)
                             .map(Song::getLyricsList)
                             .orElse(Collections.emptyList());
    }
}