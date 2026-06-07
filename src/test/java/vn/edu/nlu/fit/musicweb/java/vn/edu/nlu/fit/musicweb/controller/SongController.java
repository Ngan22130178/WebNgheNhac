package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongLyricsRepository;
import java.util.List;
@Controller
public class SongController {

    @Autowired
    private SongLyricsRepository songLyricsRepository;
    @GetMapping("/api/songs/{id}/lyrics")
    @ResponseBody
    public List<SongLyrics> getLyricsBySong(@PathVariable Long id) {
        return songLyricsRepository.findBySongId(id);
    }
}
