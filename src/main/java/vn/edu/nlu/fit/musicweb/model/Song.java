package vn.edu.nlu.fit.musicweb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "songs") // Thay thế @Entity và @Table
public class Song {
    @Id // Dùng cho MongoDB _id
    private String id; // Chuyển từ Long sang String

    @Indexed // Tăng tốc độ tìm kiếm
    private String title;

    private String artist;
    private String url; 
    @Indexed
    private String genre;
    private String albumName; 

    // MongoDB nhúng trực tiếp đối tượng SongLyrics vào đây
    private List<SongLyrics> lyricsList = new ArrayList<>();

    public Song() {}

    public Song(String title, String artist, String url, String genre, String albumName) {
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.genre = (genre != null) ? genre : "Chưa xác định";
        this.albumName = (albumName != null) ? albumName : "Chưa xác định";
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }

    public List<SongLyrics> getLyricsList() { return lyricsList; }
    public void setLyricsList(List<SongLyrics> lyricsList) { this.lyricsList = lyricsList; }
    
    // Helper method
    public void addLyrics(SongLyrics lyrics) {
        this.lyricsList.add(lyrics);
    }
}