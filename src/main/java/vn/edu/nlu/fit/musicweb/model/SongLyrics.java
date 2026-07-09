package vn.edu.nlu.fit.musicweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "song_lyrics")
@Getter @Setter @NoArgsConstructor
public class SongLyrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ngôn ngữ: "vi", "en", "jp", "zh"
    @Column(name = "language", length = 10)
    private String language; 

    // Định dạng: "LRC" hoặc "TXT"
    @Column(name = "format", length = 5)
    private String format; 

    // Url của file lời
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    // Lưu kích thước file
    @Column(name = "file_size")
    private Long fileSize;

    // Quan hệ ManyToOne với Song
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    // Helper: Giúp truy xuất ID dễ dàng trong logic code
    public Long getSongId() {
        return song != null ? song.getId() : null;
    }
}