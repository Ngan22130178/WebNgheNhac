package vn.edu.nlu.fit.musicweb.controller;

import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<Song>>> searchSongs(@RequestParam("q") String query) {
        List<Song> songs = songRepository.findByTitleContainingIgnoreCase(query);

        ResponseDTO<List<Song>> response = new ResponseDTO<>(true, "Tìm kiếm thành công!", songs);
        return ResponseEntity.ok(response);
    }
}