package vn.edu.nlu.fit.musicweb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.repository.SongLyricsRepository;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner initDatabase(SongRepository songRepo, SongLyricsRepository lyricsRepo) {
        return args -> {
            // Xóa dữ liệu cũ để tránh trùng lặp khi chạy lại ứng dụng
            songRepo.deleteAll();
            lyricsRepo.deleteAll();

            // Danh sách các bài hát cần thêm
            addSong(songRepo, lyricsRepo, "Nhất Tư Bách Hài Bất Tự Do", 
            "Ca sĩ 1", "/audio/nhat_tu_bach_hai_bat_tu_do.mp3",
            "Nhạc Trẻ", "Album Hè 2026", "vi", 
            "/audio/nhat_tu_bach_hai_bat_tu_do.lrc");

            addSong(songRepo, lyricsRepo, "Vân Sơn Ký Tuyết", 
            "Tả Từ", "/audio/van_son_ky_tuyet_ta_tu.mp3", 
            "Bolero", "Tuyển Tập Tả Từ", "vi", 
            "/audio/van_son_ky_tuyet_ta_tu.lrc");

            addSong(songRepo, lyricsRepo, "Cause I Love You", 
            "Nghệ sĩ A", "/audio/causeiloveu.mp3", "Pop", 
            "Single", "en", "/audio/causeiloveu.lrc");

            addSong(songRepo, lyricsRepo, "Dancing With Your Ghost", 
            "Sasha Alex Sloan", "/audio/dancingwithyourghost.mp3", 
            "Pop", "Single", "en", 
            "/audio/dancingwithyourghost.lrc");

            addSong(songRepo, lyricsRepo, "Em Của Ngày Hôm Qua", 
            "Sơn Tùng M-TP", "/audio/emcuangayhomqua.mp3", 
            "V-Pop", "Album 1", "vi", 
            "/audio/emcuangayhomqua.lrc");

            addSong(songRepo, lyricsRepo, "Em Là Cô Dâu Đẹp Nhất", 
            "Nghệ sĩ B", "/audio/emlacodaudepnhat.mp3", 
            "Nhạc Trẻ", "Album 1", "vi", 
            "/audio/emlacodaudepnhat.lrc");
            
            addSong(songRepo, lyricsRepo, "Monsters", "Katie Sky", 
            "/audio/monsters.mp3", "Pop", "Single", "en", 
            "/audio/monsters.lrc");
        };
    }

    // Hàm hỗ trợ để code gọn hơn và không bị lặp lại
    private void addSong(SongRepository songRepo, SongLyricsRepository lyricsRepo, 
                         String title, String artist, String url, String genre, String album, 
                         String lang, String lrcUrl) {
        Song s = songRepo.save(new Song(title, artist, url, genre, album));
        // Use no-arg constructor and setters to avoid relying on a specific SongLyrics constructor
        SongLyrics sl = new SongLyrics();
        sl.setSong(s);
        sl.setLanguage(lang);
        sl.setFormat("LRC");
        sl.setFileUrl(lrcUrl);
        lyricsRepo.save(sl);
    }
}