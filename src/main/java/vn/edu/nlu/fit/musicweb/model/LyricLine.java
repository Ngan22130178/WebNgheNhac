package vn.edu.nlu.fit.musicweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // Tự động tạo Getter, Setter, toString, equals, hashCode
@AllArgsConstructor  // Tự động tạo Constructor với tất cả tham số
@NoArgsConstructor   // Tự động tạo Constructor không tham số
public class LyricLine {
    private double time;    // Thời gian tính bằng giây (ví dụ: 10.5)
    private String content; // Nội dung lời của câu hát đó
}