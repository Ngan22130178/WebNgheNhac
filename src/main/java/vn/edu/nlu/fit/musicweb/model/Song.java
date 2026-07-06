package vn.edu.nlu.fit.musicweb.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "songs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @OneToMany(
    mappedBy = "song", 
    cascade = CascadeType.ALL, // Khi lưu/xóa bài hát, tự động xóa tất cả lời bài hát liên quan
    fetch = FetchType.LAZY, // Tải lời bài hát khi cần, không tải ngay khi lấy bài hát
    orphanRemoval = true // Khi xóa lời bài hát khỏi danh sách lyricsList, tự động xóa bản ghi trong DB nếu không còn tham chiếu nào đến nó
    )
    private List<SongLyrics> lyricsList = new ArrayList<>();

    @Builder
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
    
    // Thêm và xóa lời bài hát từ danh sách lyricsList, đồng thời cập nhật quan hệ 2 chiều
    public void addLyrics(SongLyrics lyrics) {
        this.lyricsList.add(lyrics);
        lyrics.setSong(this);
    }

    public void removeLyrics(SongLyrics lyric) {
    lyricsList.remove(lyric);
    lyric.setSong(null);
    }
}