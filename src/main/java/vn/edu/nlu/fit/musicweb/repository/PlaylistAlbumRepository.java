package vn.edu.nlu.fit.musicweb.repository;

import vn.edu.nlu.fit.musicweb.model.PlaylistAlbum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistAlbumRepository extends MongoRepository<PlaylistAlbum, String> {
    // tự động lọc theo ownerId và type  
    List<PlaylistAlbum> findByOwnerIdAndType(String ownerId, String type);
}
