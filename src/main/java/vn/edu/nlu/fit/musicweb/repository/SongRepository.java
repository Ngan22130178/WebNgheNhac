package vn.edu.nlu.fit.musicweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.nlu.fit.musicweb.model.Song;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // ========================================================================
    // 1. NHÓM TÌM KIẾM DỮ LIỆU (Search)
    // ========================================================================
    
    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> findByTitleContainingIgnoreCase(@Param("keyword") String keyword);

    @Query("SELECT s FROM Song s WHERE LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> findByArtistContainingIgnoreCase(@Param("keyword") String keyword);

    // ========================================================================
    // 2. NHÓM DANH MỤC LỌC (Distinct Filters)
    // Dùng để đổ dữ liệu vào các menu Thể loại, Album, Ca sĩ
    // ========================================================================

    @Query("SELECT DISTINCT s.genre FROM Song s WHERE s.genre IS NOT NULL AND s.genre <> ''")
    List<String> findDistinctGenres();

    @Query("SELECT DISTINCT s.albumName FROM Song s WHERE s.albumName IS NOT NULL AND s.albumName <> ''")
    List<String> findDistinctAlbums();

    @Query("SELECT DISTINCT s.artist FROM Song s WHERE s.artist IS NOT NULL AND s.artist <> ''")
    List<String> findDistinctArtists();

    // ========================================================================
    // 3. NHÓM TÌM KIẾM ĐA TRƯỜNG (Multi-Field Search)
    // Dùng để tìm kiếm bài hát theo nhiều tiêu chí trong một truy vấn duy nhất
    // ========================================================================
    @Query("SELECT s FROM Song s WHERE " +
       "LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(s.albumName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(s.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> findByKeyword(@Param("keyword") String keyword);

}
