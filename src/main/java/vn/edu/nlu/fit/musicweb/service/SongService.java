package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.nlu.fit.musicweb.model.LyricLine;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;

    @Autowired
private vn.edu.nlu.fit.musicweb.repository.UserRepository userRepository;

    // READ: Xem tất cả
    public List<Song> getAllSongs() { return songRepository.findAll(); }
    
    // READ: Xem theo ID
    public Song getSongById(Long id) { return songRepository.findById(id).orElse(null); }

    // CREATE / UPDATE: Lưu (Spring Data JPA tự hiểu nếu có ID là Update, không có ID là Create)
    public void saveSong(Song song) { songRepository.save(song); }

    // DELETE: Xóa
    public void deleteSong(Long id) { songRepository.deleteById(id); }

    // UPDATE: Cập nhật bài hát theo ID (sửa đổi các trường dữ liệu)
    public void updateSong(Long id, Song updatedSong) {
        // 1. Tìm song cũ từ DB bằng ID
        Song existingSong = songRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát với ID: " + id));

        // 2. Cập nhật các trường cơ bản
        existingSong.setTitle(updatedSong.getTitle());
        existingSong.setArtist(updatedSong.getArtist());
        existingSong.setUrl(updatedSong.getUrl());
        existingSong.setGenre(updatedSong.getGenre());
        existingSong.setAlbumName(updatedSong.getAlbumName());

        // 3. Lưu lại (đã có ID nên JPA sẽ thực hiện UPDATE)
        songRepository.save(existingSong);

    }

    // Lấy bài hát theo Thể loại 
    public List<Song> getSongsByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }

    // Lấy bài hát theo Tên Album 
    public List<Song> getSongsByAlbumName(String albumName) {
        return songRepository.findByAlbumName(albumName);
    }

    public List<Song> getFavoriteSongsByUser(String username) {
        return userRepository.findFavoriteSongsByUsername(username);
    }

    @org.springframework.transaction.annotation.Transactional
public boolean toggleFavorite(String username, Long songId) {
    // 1. Tìm User dựa trên email/username
    vn.edu.nlu.fit.musicweb.model.User user = userRepository.findByEmail(username) 
        .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

    // 2. Tìm bài hát dựa trên songId
    Song song = songRepository.findById(songId)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát"));

    // 3. Lấy danh sách bài hát yêu thích hiện tại của User ra (Mapped bởi JPA)
    List<Song> favoriteSongs = user.getFavoriteSongs();
    if (favoriteSongs == null) {
        favoriteSongs = new ArrayList<>();
    }

    boolean isAlreadyLiked = favoriteSongs.stream().anyMatch(s -> s.getId().equals(songId));

    if (isAlreadyLiked) {
        // Nếu đã thích -> Xóa khỏi danh sách đối tượng
        favoriteSongs.removeIf(s -> s.getId().equals(songId));
        userRepository.save(user); // Hibernate tự động xóa bản ghi ở bảng trung gian
        return false; 
    } else {
        // Nếu chưa thích -> Thêm vào danh sách đối tượng
        favoriteSongs.add(song);
        userRepository.save(user); // Hibernate tự động thêm bản ghi vào bảng trung gian
        return true; 
    }
}

    // Regex cải tiến: Bắt từng tag thời gian, hỗ trợ các dòng có nhiều tag
    private static final Pattern TIME_PATTERN = Pattern.compile("\\[(\\d{2}):(\\d{2})[\\.:](\\d{2,3})\\]");

    public List<LyricLine> parseLrcFile(String rawLrcContent) {
        List<LyricLine> lyricLines = new ArrayList<>();

        if (rawLrcContent == null || rawLrcContent.isBlank()) {
            return lyricLines;
        }

        String[] lines = rawLrcContent.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Tách nội dung lời hát ra khỏi các tag thời gian
            // Ví dụ: "[00:12.34][00:13.00] Hello" -> content = "Hello"
            String content = line.replaceAll("\\[.*?\\]", "").trim();
            
            // Tìm tất cả các tag thời gian trong dòng (ví dụ: [00:12.34] và [00:13.00])
            Matcher matcher = TIME_PATTERN.matcher(line);
            while (matcher.find()) {
                double minutes = Double.parseDouble(matcher.group(1));
                double seconds = Double.parseDouble(matcher.group(2));
                double ms = Double.parseDouble(matcher.group(3));

                // Chuẩn hóa mili-giây
                if (matcher.group(3).length() == 2) ms *= 10;

                double totalSeconds = (minutes * 60) + seconds + (ms / 1000.0);
                
                // Thêm một LyricLine cho mỗi timestamp tìm thấy
                if (!content.isEmpty()) {
                    lyricLines.add(new LyricLine(totalSeconds, content));
                }
            }
        }

        // Sắp xếp thời gian tăng dần
        lyricLines.sort(Comparator.comparingDouble(LyricLine::getTime));
        return lyricLines;
    }
}