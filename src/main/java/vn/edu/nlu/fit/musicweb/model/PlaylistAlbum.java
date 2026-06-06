package vn.edu.nlu.fit.musicweb.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "playlists_albums")
public class PlaylistAlbum {
    @Id
    private String id;
    private String name;
    private String ownerId;       
    private String type;         
    private List<String> songIds; 
}
