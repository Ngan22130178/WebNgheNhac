package vn.edu.nlu.fit.musicweb.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity 
@Table(name = "users") 
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password; 
    private String googleId; 

    @Column(nullable = false)
    private String role;

    private String fullName;
    private String avatarUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_favorite_songs",                  // Tên bảng trung gian sẽ tự sinh trong DB
        joinColumns = @JoinColumn(name = "user_id"),    // Liên kết tới id của bảng users
        inverseJoinColumns = @JoinColumn(name = "song_id") // Liên kết tới id của bảng songs
    )
    private List<Song> favoriteSongs = new java.util.ArrayList<>();

    @Column(nullable = true)
    private String provider = "LOCAL"; 

    // Trạng thái hoạt động của người dùng
    @Column(nullable = false)
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }
}