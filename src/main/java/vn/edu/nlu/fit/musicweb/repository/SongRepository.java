package vn.edu.nlu.fit.musicweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.nlu.fit.musicweb.model.Song;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Không cần viết thêm code, bạn đã có sẵn các lệnh findAll(), save(), findById(), ...

    // Tìm kiếm bài hát theo tên 
    List<Song> findByTitleContainingIgnoreCase(String title);

}