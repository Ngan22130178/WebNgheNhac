package vn.edu.nlu.fit.musicweb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.edu.nlu.fit.musicweb.model.*;
import vn.edu.nlu.fit.musicweb.repository.*;
import java.util.List;
@Configuration
public class DataSeeder {

    // Dữ liệu mẫu cho các bài hát và lời bài hát    
    @Bean
    CommandLineRunner initDatabase(SongRepository songRepo, SongLyricsRepository lyricsRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
                songRepo.deleteAll();
                lyricsRepo.deleteAll();
                // 1. Khởi tạo dữ liệu người dùng (User)
                if (userRepo.count() == 0) {
                        // Khởi tạo User với mật khẩu đã mã hóa
                        List<User> users = List.of(
                                
                                User.builder()
                                .email("admin@musicweb.com")
                                .password(passwordEncoder.encode("admin123")) // Mã hóa ở đây
                                .role("ROLE_ADMIN")
                                .fullName("Quản trị hệ thống")
                                .enabled(true)
                                .provider("LOCAL")
                                .build(),
                                User.builder()
                                .email("nguyenvana@gmail.com")
                                .password(passwordEncoder.encode("user123")) // Mã hóa ở đây
                                .role("ROLE_USER")
                                .fullName("Nguyễn Văn A")
                                .enabled(true)
                                .provider("LOCAL")
                                .build(),
                                User.builder()
                                .email("tranthib@gmail.com")
                                .password(passwordEncoder.encode("user123"))
                                .role("ROLE_USER")
                                .fullName("Trần Thị B")
                                .enabled(true)
                                .provider("LOCAL")
                                .build(),
                                User.builder()
                                .email("lequangc@gmail.com")
                                .password(passwordEncoder.encode("user123"))
                                .role("ROLE_USER")
                                .fullName("Lê Quang C")
                                .enabled(true)
                                .provider("LOCAL")
                                .build(),
                                User.builder()
                                .email("phamthid@gmail.com")
                                .password(passwordEncoder.encode("user123"))
                                .role("ROLE_USER")
                                .fullName("Phạm Thị D")
                                .enabled(false)
                                .provider("LOCAL")
                                .build()
                        );
                        userRepo.saveAll(users);
                        System.out.println("Đã khởi tạo thành công 5 người dùng!");
                }
                
                // Khởi tạo dữ liệu bài hát và lời bài hát
                // Cấu trúc: addSong(songRepo, lyricsRepo, Title, Artist, Url, Genre, Album, Lang, FilePath)

                // --- Các bài hát hiện có ---
                addSong(songRepo, lyricsRepo, "Nhất Tư Bách Hài Bất Tự Do", "Ca sĩ 1", "/audio/nhat_tu_bach_hai_bat_tu_do.mp3", "Nhạc Trẻ", "Album Hè 2026", "zh", "/audio/nhat_tu_bach_hai_bat_tu_do.lrc");
                addSong(songRepo, lyricsRepo, "Vân Sơn Ký Tuyết", "Tả Từ", "/audio/van_son_ky_tuyet_ta_tu.mp3", "Bolero", "Tuyển Tập Tả Từ", "zh", "/audio/van_son_ky_tuyet_ta_tu.lrc");
                addSong(songRepo, lyricsRepo, "Cause I Love You", "Nghệ sĩ A", "/audio/causeiloveu.mp3", "Pop", "Single", "en", "/audio/causeiloveu.lrc");
                addSong(songRepo, lyricsRepo, "Dancing With Your Ghost", "Sasha Alex Sloan", "/audio/dancingwithyourghost.mp3", "Pop", "Single", "en", "/audio/dancingwithyourghost.lrc");
                addSong(songRepo, lyricsRepo, "Em Của Ngày Hôm Qua", "Sơn Tùng M-TP", "/audio/emcuangayhomqua.mp3", "V-Pop", "Album 1", "vi", "/audio/emcuangayhomqua.lrc");
                addSong(songRepo, lyricsRepo, "Em Là Cô Dâu Đẹp Nhất", "Nghệ sĩ B", "/audio/emlacodaudepnhat.mp3", "Nhạc Trẻ", "Album 1", "vi", "/audio/emlacodaudepnhat.lrc");
                addSong(songRepo, lyricsRepo, "Monsters", "Katie Sky", "/audio/monsters.mp3", "Pop", "Single", "en", "/audio/monsters.lrc");
                addSong(songRepo, lyricsRepo, "Waiting For Love", "Avicii", "/audio/waitingforlove.mp3", "EDM", "Stories", "en", "/audio/waitingforlove.lrc");
                addSong(songRepo, lyricsRepo, "Ánh Nắng Của Anh", "Đức Phúc", "/audio/anhnangcuaanh.mp3", "Nhạc Trẻ", "OST Chờ Em Đến Ngày Mai", "vi", "/audio/anhnangcuaanh.lrc");
                addSong(songRepo, lyricsRepo, "Shape of You", "Ed Sheeran", "/audio/shapeofyou.mp3", "Pop", "Divide", "en", "/audio/shapeofyou.lrc");
                addSong(songRepo, lyricsRepo, "Ngày Mai Người Ta Lấy Chồng", "Thành Đạt", "/audio/ngaymainguoitalaychong.mp3", "Bolero", "Single", "vi", "/audio/ngaymainguoitalaychong.lrc");
                addSong(songRepo, lyricsRepo, "Faded", "Alan Walker", "/audio/faded.mp3", "EDM", "Different World", "en", "/audio/faded.lrc");
                addSong(songRepo, lyricsRepo, "Nơi Này Có Anh", "Sơn Tùng M-TP", "/audio/noinaycoanh.mp3", "V-Pop", "Single", "vi", "/audio/noinaycoanh.lrc");
                addSong(songRepo, lyricsRepo, "Until I Found You", "Stephen Sanchez", "/audio/untilifoundyou.mp3", "Pop", "Easy On My Eyes", "en", "/audio/untilifoundyou.lrc");
                addSong(songRepo, lyricsRepo, "Bên Trên Tầng Lầu", "Tăng Duy Tân", "/audio/bentrentanglau.mp3", "Nhạc Trẻ", "Single", "vi", "/audio/bentrentanglau.lrc");
                addSong(songRepo, lyricsRepo, "Ngã Lai", "Tôn Sách", "/audio/Ngã_Lai-Tôn_Sách.mp3", "Nhạc Trẻ", "Đại Hào Diên", "chinese", "/audio/Ngã_Lai-Tôn_Sách.lrc");
                addSong(songRepo, lyricsRepo, "Hoa Soi Sáng Nơi Đây", "Nguyệt Mộng", "/audio/Hoa_soi_sáng_nơi_đây-Nguyệt_Mộng.mp3", "Nhạc Trẻ", "Album Mới", "vi", "/audio/Hoa_soi_sáng_nơi_đây-Nguyệt_Mộng.lrc");

                // --- BÀI HÁT MỚI THÊM VÀO ---
                // Ví dụ bài hát có cả 2 ngôn ngữ hoặc 2 định dạng khác nhau
                // --- Cập nhật "Vân Sơn Ký Tuyết" ---
                // Phiên bản tiếng Việt (.lrc)
                addSong(songRepo, lyricsRepo, "Vân Sơn Ký Tuyết", "Tả Từ", "/audio/van_son_ky_tuyet_ta_tu.mp3", 
                        "Bolero", "Tuyển Tập Tả Từ", "vi", "/audio/van_son_ky_tuyet_ta_tu.lrc");

                // Nếu bạn có thêm phiên bản lời khác, ví dụ tiếng Trung (.txt)
                addSong(songRepo, lyricsRepo, "Vân Sơn Ký Tuyết", "Tả Từ", "/audio/van_son_ky_tuyet_ta_tu.mp3", 
                        "Bolero", "Tuyển Tập Tả Từ", "zh", "/audio/van_son_ky_tuyet_ta_tu_zh.txt");


                // --- Cập nhật "Nhất Tư Bách Hài Bất Tự Do" ---
                // Phiên bản tiếng Việt (.lrc)
                addSong(songRepo, lyricsRepo, "Nhất Tư Bách Hài Bất Tự Do", "Ca sĩ 1", "/audio/nhat_tu_bach_hai_bat_tu_do.mp3", 
                        "Nhạc Trẻ", "Album Hè 2026", "vi", "/audio/nhat_tu_bach_hai_bat_tu_do.lrc");

                // Ví dụ bạn có thêm bản dịch sang tiếng Anh (.txt)
                addSong(songRepo, lyricsRepo, "Nhất Tư Bách Hài Bất Tự Do", "Ca sĩ 1", "/audio/nhat_tu_bach_hai_bat_tu_do.mp3", 
                        "Nhạc Trẻ", "Album Hè 2026", "zh", "/audio/nhat_tu_bach_hai_bat_tu_do_zh.txt");

        };
  
    }

        private void addSong(SongRepository songRepo, SongLyricsRepository lyricsRepo, 
                     String title, String artist, String url, String genre, 
                     String album, String lang, String filePath) {
    
                // 1. Tìm hoặc tạo Song
                Song song = songRepo.findByTitle(title).orElse(null);
                
                if (song == null) {
                        song = Song.builder()
                                .title(title)
                                .artist(artist)
                                .url(url)
                                .genre(genre)
                                .albumName(album)
                                .build();
                        song = songRepo.save(song); // Lưu và lấy lại đối tượng song đã có ID
                }

                // 2. Tạo đối tượng Lyrics
                String format = filePath.toLowerCase().endsWith(".lrc") ? "LRC" : "TXT";
                SongLyrics lyrics = new SongLyrics();
                lyrics.setLanguage(lang);
                lyrics.setFormat(format);
                lyrics.setFileUrl(filePath);
                lyrics.setSong(song);
                
                // 3. Lưu lyrics
                lyricsRepo.save(lyrics); 
        }
}