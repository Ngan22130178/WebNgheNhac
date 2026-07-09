package vn.edu.nlu.fit.musicweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongLyricsRepository;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/lyrics")
public class SongLyricsController {

    @Autowired
    private SongLyricsRepository lyricsRepository;

    /**
     * API lấy nội dung lời bài hát
     * @param id ID của bài hát
     * @param lang Ngôn ngữ (ví dụ: vi, en, jp). Nếu null sẽ lấy bản đầu tiên tìm thấy.
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getLyricsContent(
            @PathVariable Long id,
            @RequestParam(required = false) String lang) {

        SongLyrics lyrics;
        
        // 1. Truy vấn dựa trên ngôn ngữ
        if (lang != null && !lang.isEmpty()) {
            lyrics = lyricsRepository.findBySongIdAndLanguage(id, lang);
        } else {
            lyrics = lyricsRepository.findFirstBySongId(id);
        }

        if (lyrics == null || lyrics.getFileUrl() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chưa có lời bài hát này.");
        }

        Path sourcePath = Paths.get(lyrics.getFileUrl());
        if (!Files.exists(sourcePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File lời không tồn tại trên hệ thống.");
        }

        try {
            // 2. Xử lý định dạng TXT (Đọc trực tiếp)
            if ("TXT".equalsIgnoreCase(lyrics.getFormat())) {
                return ResponseEntity.ok(Files.readString(sourcePath));
            }

            // 3. Xử lý định dạng LRC (Caching sang TXT)
            if ("LRC".equalsIgnoreCase(lyrics.getFormat())) {
                return handleLrcToTxtConversion(sourcePath);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Định dạng file không hỗ trợ.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi đọc file lời.");
        }
    }

    /**
     * Logic cache: Chuyển LRC sang TXT và lưu lại để dùng cho các lần sau
     */
    private ResponseEntity<String> handleLrcToTxtConversion(Path lrcPath) throws IOException {
        String pathStr = lrcPath.toString();
        // Tạo đường dẫn file .txt cùng thư mục
        Path txtPath = Paths.get(pathStr.substring(0, pathStr.lastIndexOf(".")) + ".txt");

        if (Files.exists(txtPath)) {
            // Nếu đã có bản convert, trả về ngay (Rất nhanh)
            return ResponseEntity.ok(Files.readString(txtPath));
        } else {
            // Nếu chưa có, convert và lưu lại
            String lrcContent = Files.readString(lrcPath);
            String txtContent = convertLrcToTxt(lrcContent);
            Files.writeString(txtPath, txtContent);
            return ResponseEntity.ok(txtContent);
        }
    }

    private String convertLrcToTxt(String lrcContent) {
        // Loại bỏ tag thời gian [00:00.00] và các tag metadata [ar:..], [ti:..]
        return lrcContent.replaceAll("\\[\\d{2}:\\d{2}\\.?\\d{0,2}\\]", "")
                         .replaceAll("\\[.*?:.*?\\]", "")
                         .trim();
    }
}