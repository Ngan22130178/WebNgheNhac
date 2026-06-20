package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.*;
import vn.edu.nlu.fit.musicweb.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/songs")
public class SongController {

    @Autowired private SongRepository songRepository;
    @Autowired private SongLyricsRepository songLyricsRepository;

    // ========================================================================
    // 1. NHÓM NẠP BẢNG DỮ LIỆU (VIEW FRAGMENTS)
    // ========================================================================
    
   @GetMapping("/all")
    public String getAllSongs(Model model) {
        List<Song> songs = songRepository.findAll();
        
        // Debug: Kiểm tra xem DB có trả về dữ liệu không
        System.out.println("DEBUG: Dữ liệu bài hát lấy từ DB: " + (songs == null ? "NULL" : songs.size() + " bản ghi"));
        
        model.addAttribute("songs", songs);
        return "fragments/songs_table";
    }

    // ========================================================================
    // 2. NHÓM LỌC VÀ TÌM KIẾM
    // ========================================================================

    @GetMapping("/filter")
    public String filterSongs(@RequestParam String type, @RequestParam String value, Model model) {
        List<Song> songs = songRepository.findAll();
        List<Song> filtered = songs.stream().filter(s -> {
            if ("genre".equals(type)) return s.getGenre() != null && s.getGenre().equalsIgnoreCase(value);
            if ("album".equals(type)) return s.getAlbumName() != null && s.getAlbumName().equalsIgnoreCase(value);
            if ("artist".equals(type)) return s.getArtist() != null && s.getArtist().equalsIgnoreCase(value);
            return false;
        }).collect(Collectors.toList());

        model.addAttribute("songs", filtered);
        // Quan trọng: Trả về cùng path với trang chủ để HTMX render đúng
        return "fragments/songs_table"; 
    }

    @GetMapping("/search")
    public String searchSongs(@RequestParam(value = "q") String keyword, Model model) {
        // Gọi trực tiếp hàm tìm kiếm đa trường
        List<Song> result = songRepository.findByKeyword(keyword);
        
        model.addAttribute("songs", result);
        model.addAttribute("keyword", keyword);
        
        // Trả về fragment để HTMX thay thế bảng
        return "fragments/songs_table";
    }

    // ========================================================================
    // 3. NHÓM API HỖ TRỢ (DROP-DOWNS & DATA)
    // ========================================================================

    @GetMapping("/categories/{type}")
    @ResponseBody
    public String getCategories(@PathVariable String type) {
        List<String> list;
        switch (type) {
            case "genre":  list = songRepository.findDistinctGenres(); break;
            case "album":  list = songRepository.findDistinctAlbums(); break;
            case "artist": list = songRepository.findDistinctArtists(); break;
            default:       list = Collections.emptyList();
        }

        return list.stream()
            .map(item -> "<li><a class='dropdown-item' href='#' hx-get='/api/songs/filter?type=" + type + "&value=" + item + "' hx-target='#songListBody'>" + item + "</a></li>")
            .collect(Collectors.joining());
    }

    @GetMapping("/lyrics/{id}")
    @ResponseBody
    public String getLyricsApi(@PathVariable Long id) {
        return songLyricsRepository.findBySongId(id).stream()
                .findFirst()
                .map(SongLyrics::getContent)
                .orElse("Chưa có lời bài hát.");
    }
}