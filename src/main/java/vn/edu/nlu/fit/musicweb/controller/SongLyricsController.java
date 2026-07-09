package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongLyricsRepository;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/songs")
public class SongLyricsController {

    @Autowired
    private SongLyricsRepository lyricsRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/lyrics/{id}")
    public ResponseEntity<String> getLyricsContent(
            @PathVariable Long id,
            @RequestParam(required = false) String lang) {

        SongLyrics lyrics;
        
        if (lang != null && !lang.isEmpty()) {
            lyrics = lyricsRepository.findBySongIdAndLanguage(id, lang);
        } else {
            lyrics = lyricsRepository.findFirstBySongId(id);
        }

        if (lyrics == null || lyrics.getFileUrl() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chưa có lời bài hát này.");
        }

        try {
            // Đọc file từ thư mục static của dự án
            Resource resource = resourceLoader.getResource("classpath:static" + lyrics.getFileUrl());
            
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File lời không tồn tại trên hệ thống.");
            }

            Path sourcePath = Paths.get(resource.getURI());

            // Xử lý file TXT
            if ("TXT".equalsIgnoreCase(lyrics.getFormat())) {
                return ResponseEntity.ok(Files.readString(sourcePath));
            }

            // Xử lý file LRC (Tự convert bỏ mốc thời gian)
            if ("LRC".equalsIgnoreCase(lyrics.getFormat())) {
                return handleLrcToTxtConversion(sourcePath);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Định dạng file không hỗ trợ.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi đọc file lời.");
        }
    }

    private ResponseEntity<String> handleLrcToTxtConversion(Path lrcPath) throws IOException {
        String pathStr = lrcPath.toString();
        Path txtPath = Paths.get(pathStr.substring(0, pathStr.lastIndexOf(".")) + ".txt");

        if (Files.exists(txtPath)) {
            return ResponseEntity.ok(Files.readString(txtPath));
        } else {
            String lrcContent = Files.readString(lrcPath);
            String txtContent = convertLrcToTxt(lrcContent);
            Files.writeString(txtPath, txtContent);
            return ResponseEntity.ok(txtContent);
        }
    }

    private String convertLrcToTxt(String lrcContent) {
        return lrcContent.replaceAll("\\[\\d{2}:\\d{2}\\.?\\d{0,2}\\]", "")
                         .replaceAll("\\[.*?:.*?\\]", "")
                         .trim();
    }
}