package vn.edu.nlu.fit.musicweb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "songs")
public class Song {
    @Id
    private String id;
    private String title;
    private String artist;
    private String audioUrl;
    private String uploaderId;
    private Map<String, List<LyricLine>> lyricsMap;

    @Data
    public static class LyricLine {
        private Double time;
        private String content;
    }
}
