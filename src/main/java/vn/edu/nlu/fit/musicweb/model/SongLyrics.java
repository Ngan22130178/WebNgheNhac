package vn.edu.nlu.fit.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Tự động tạo Getter, Setter, toString, equals, hashCode
@AllArgsConstructor // Tự động tạo Constructor đầy đủ tham số
@NoArgsConstructor // Tự động tạo Constructor không tham số
public class SongLyrics {
    private String language; // "vi", "en", "zh"
    private String format;   // "LRC" hoặc "TEXT"
    private String content;  // Nội dung lời
    private String fileUrl;  // Đường dẫn file (nếu có)
}