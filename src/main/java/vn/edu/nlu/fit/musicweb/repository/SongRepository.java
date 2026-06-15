package vn.edu.nlu.fit.musicweb.repository;

import vn.edu.nlu.fit.musicweb.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {
    
    // Tìm kiếm bài hát theo tên (hỗ trợ không phân biệt hoa thường)
    List<Song> findByTitleContainingIgnoreCase(String title);
    
    // Tìm kiếm bài hát theo thể loại (hỗ trợ lọc ở tầng Database)
    List<Song> findByGenreIgnoreCase(String genre);
    
    // Tìm kiếm theo nghệ sĩ (bổ sung thường dùng cho web nhạc)
    List<Song> findByArtistIgnoreCase(String artist);
}