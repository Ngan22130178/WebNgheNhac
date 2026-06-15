package vn.edu.nlu.fit.musicweb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.model.SongLyrics;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(SongRepository songRepo) {
        return args -> {
            // 1. Xóa dữ liệu cũ
            songRepo.deleteAll();

            // 2. Thêm dữ liệu mẫu
            saveSong(songRepo, "Nhất Tư Bách Hài Bất Tự Do", "Ca sĩ 1", "/audio/nhat.mp3", "Nhạc Trẻ", "Album Hè 2026", "vi", "/audio/nhat.lrc");
            saveSong(songRepo, "Vân Sơn Ký Tuyết", "Tả Từ", "/audio/van.mp3", "Bolero", "Tuyển Tập Tả Từ", "vi", "/audio/van.lrc");
            saveSong(songRepo, "Dancing With Your Ghost", "Sasha Alex Sloan", "/audio/ghost.mp3", "Pop", "Single", "en", "/audio/ghost.lrc");
        };
    }

    private void saveSong(SongRepository songRepo, String title, String artist, String url, 
                          String genre, String album, String lang, String lrcUrl) {
        
        // Tạo bài hát mới
        Song song = new Song(title, artist, url, genre, album);
        
        // Tạo lời bài hát và nhúng trực tiếp vào list
        SongLyrics lyrics = new SongLyrics(lang, "LRC", null, lrcUrl);
        song.setLyricsList(new ArrayList<>(List.of(lyrics)));
        
        // Lưu bài hát (MongoDB sẽ lưu cả lyrics bên trong luôn)
        songRepo.save(song);
    }
}