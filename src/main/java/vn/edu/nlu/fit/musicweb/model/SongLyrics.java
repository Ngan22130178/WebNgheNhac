package vn.edu.nlu.fit.musicweb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "song_lyrics")
public class SongLyrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(nullable = false)
    private String language; // "vi", "en", "zh"

    @Column(nullable = false)
    private String format;   // "LRC" hoặc "TEXT"

    @Column(columnDefinition = "TEXT")
    private String content;  // Lưu lời trực tiếp nếu cần

    private String fileUrl;  // Hoặc đường dẫn file nếu file lời lớn

    public SongLyrics() {}

    // Getters and Setters
    public Long getId() { return id; }
    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}