package vn.edu.nlu.fit.musicweb.repository;

import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongLyricsRepository extends MongoRepository<SongLyrics, String> {
    // Tìm lời bài hát theo ID của bài hát cha
    List<SongLyrics> findBySongId(String songId);
}