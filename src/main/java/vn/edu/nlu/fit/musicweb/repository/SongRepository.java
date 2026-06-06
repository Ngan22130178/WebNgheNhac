package vn.edu.nlu.fit.musicweb.repository;

import vn.edu.nlu.fit.musicweb.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {
    //lấy bài hát cá nhân theo uploaderId
    List<Song> findByUploaderId(String uploaderId);
}
