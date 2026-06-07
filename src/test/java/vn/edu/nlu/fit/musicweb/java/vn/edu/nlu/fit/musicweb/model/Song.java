package vn.edu.nlu.fit.musicweb.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String artist;
    private String url; 
    private String genre; 
    private String albumName; 

    // Quan hệ 1-N: Lưu danh sách lời bài hát
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SongLyrics> lyricsList = new ArrayList<>();

    public Song() {}

    // Constructor chuẩn (không bao gồm id vì DB tự sinh)
    public Song(String title, String artist, String url, String genre, String albumName) {
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.genre = (genre != null) ? genre : "Chưa xác định";
        this.albumName = (albumName != null) ? albumName : "Chưa xác định";
    }

    // Các Getter và Setter đầy đủ
    public Long getId() { return id; }
    
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
    
    // Helper method để thêm lời vào bài hát (giúp đồng bộ 2 chiều)
    public void addLyrics(SongLyrics lyrics) {
        lyricsList.add(lyrics);
        lyrics.setSong(this);
    }
}