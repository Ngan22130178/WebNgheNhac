package vn.edu.nlu.fit.musicweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import java.util.List;

public interface SongLyricsRepository extends JpaRepository<SongLyrics, Long> {
    List<SongLyrics> findBySongId(Long songId);
}