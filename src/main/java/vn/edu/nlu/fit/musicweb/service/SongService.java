package vn.edu.nlu.fit.musicweb.service;

import org.springframework.stereotype.Service;
import vn.edu.nlu.fit.musicweb.model.LyricLine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SongService {

    // Regex cải tiến: Bắt từng tag thời gian, hỗ trợ các dòng có nhiều tag
    private static final Pattern TIME_PATTERN = Pattern.compile("\\[(\\d{2}):(\\d{2})[\\.:](\\d{2,3})\\]");

    public List<LyricLine> parseLrcFile(String rawLrcContent) {
        List<LyricLine> lyricLines = new ArrayList<>();

        if (rawLrcContent == null || rawLrcContent.isBlank()) {
            return lyricLines;
        }

        String[] lines = rawLrcContent.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Tách nội dung lời hát ra khỏi các tag thời gian
            // Ví dụ: "[00:12.34][00:13.00] Hello" -> content = "Hello"
            String content = line.replaceAll("\\[.*?\\]", "").trim();
            
            // Tìm tất cả các tag thời gian trong dòng (ví dụ: [00:12.34] và [00:13.00])
            Matcher matcher = TIME_PATTERN.matcher(line);
            while (matcher.find()) {
                double minutes = Double.parseDouble(matcher.group(1));
                double seconds = Double.parseDouble(matcher.group(2));
                double ms = Double.parseDouble(matcher.group(3));

                // Chuẩn hóa mili-giây
                if (matcher.group(3).length() == 2) ms *= 10;

                double totalSeconds = (minutes * 60) + seconds + (ms / 1000.0);
                
                // Thêm một LyricLine cho mỗi timestamp tìm thấy
                if (!content.isEmpty()) {
                    lyricLines.add(new LyricLine(totalSeconds, content));
                }
            }
        }

        // Sắp xếp thời gian tăng dần
        lyricLines.sort(Comparator.comparingDouble(LyricLine::getTime));
        return lyricLines;
    }
}