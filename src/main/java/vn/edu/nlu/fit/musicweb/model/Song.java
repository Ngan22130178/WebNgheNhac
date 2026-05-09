package vn.edu.nlu.fit.musicweb.model;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "songs")
public class Song {
    @Id
    private String id;
    private String title;
    private String artist;
    private String album;
    private String audioUrl; // Đường dẫn file nhạc (.mp3)
    private String coverUrl; // Hình ảnh album
    
    // Danh sách các file LRC (Đa ngôn ngữ)
    private List<Lyrics> lyrics; 

    // Getters và Setters
}
