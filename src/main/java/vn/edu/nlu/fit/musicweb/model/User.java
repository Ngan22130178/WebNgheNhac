package vn.edu.nlu.fit.musicweb.model;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private List<String> favoriteSongIds; // Lưu ID các bài hát yêu thích
}