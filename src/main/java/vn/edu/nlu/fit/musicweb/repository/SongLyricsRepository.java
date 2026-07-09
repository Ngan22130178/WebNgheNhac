package vn.edu.nlu.fit.musicweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;

public interface SongLyricsRepository extends JpaRepository<SongLyrics, Long> {
 // 1. Tìm lời theo ID bài hát và Ngôn ngữ cụ thể
    // Spring sẽ tự hiểu: SELECT * FROM song_lyrics WHERE song_id = ? AND language = ?
    SongLyrics findBySongIdAndLanguage(Long songId, String language);

    // 2. Tìm bản lời đầu tiên có ID bài hát (dùng khi lang là null)
    // Spring sẽ tự hiểu: SELECT * FROM song_lyrics WHERE song_id = ? LIMIT 1
    SongLyrics findFirstBySongId(Long songId);
}