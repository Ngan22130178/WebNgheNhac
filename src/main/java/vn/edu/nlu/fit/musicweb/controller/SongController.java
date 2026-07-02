package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.*;
import vn.edu.nlu.fit.musicweb.repository.*;

import jakarta.servlet.http.HttpSession;
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

    // ========================================================================
    // 4. NHÓM QUẢN LÝ HÀNG ĐỢI (QUEUE MANAGEMENT)
    // ========================================================================

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