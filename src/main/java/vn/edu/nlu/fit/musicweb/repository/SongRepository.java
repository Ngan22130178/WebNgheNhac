package vn.edu.nlu.fit.musicweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.nlu.fit.musicweb.model.Song;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Không cần viết thêm code, bạn đã có sẵn các lệnh findAll(), save(), findById(), ...

    // Tìm kiếm bài hát theo tên 
    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> findByTitleContainingIgnoreCase(@Param("keyword") String keyword);

    // Tìm theo tên ca sĩ
    @Query("SELECT s FROM Song s WHERE LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> findByArtistContainingIgnoreCase(@Param("keyword") String keyword);

}