package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Đã thêm
import vn.edu.nlu.fit.musicweb.model.LyricLine;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.model.User; // Import Model User
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

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
    private UserRepository userRepository;
    
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
 /**
     * 1. Lấy danh sách các đối tượng Song yêu thích của User dựa vào email (username)
     * (Sửa đổi: Lấy trực tiếp từ danh sách favoriteSongs của thực thể User)
     */
    @Transactional(readOnly = true)
    public List<Song> getFavoriteSongsByUser(String username) {
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null && user.getFavoriteSongs() != null) {
            // Trả về trực tiếp danh sách thực thể Song
            return user.getFavoriteSongs();
        }
        return new ArrayList<>();
    }

    /**
     * 2. Bật/Tắt trạng thái yêu thích bài hát (Thêm đối tượng Song nếu chưa có, Xóa nếu đã có)
     * (Sửa đổi: Xử lý thêm/xóa thực thể Song thay vì chuỗi ID)
     */
    @Transactional
    public boolean toggleFavorite(String username, Long songId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài hát: " + songId));

        List<Song> favoriteSongs = user.getFavoriteSongs();
        if (favoriteSongs == null) {
            favoriteSongs = new ArrayList<>();
            user.setFavoriteSongs(favoriteSongs);
        }

        boolean isLiked;
        // Kiểm tra xem danh sách bài hát yêu thích đã chứa bài hát này chưa (Dựa vào ID)
        boolean alreadyContains = favoriteSongs.stream().anyMatch(s -> s.getId().equals(songId));

        if (alreadyContains) {
            // Nếu đã thích thì tiến hành xóa khỏi danh sách
            favoriteSongs.removeIf(s -> s.getId().equals(songId));
            isLiked = false;
        } else {
            // Nếu chưa thích thì thêm thực thể bài hát vào danh sách
            favoriteSongs.add(song);
            isLiked = true;
        }

        userRepository.save(user); // JPA tự động cập nhật bảng trung gian user_favorite_songs
        return isLiked;
    }

    /**
     * 3. Lấy danh sách chỉ chứa ID (kiểu Long) để JSTL bên giao diện dễ check trạng thái
     * (Sửa đổi: Duyệt danh sách bài hát để gom list ID kiểu Long)
     */
    @Transactional(readOnly = true)
    public List<Long> getFavoriteSongIdsByUser(String username) {
        List<Long> ids = new ArrayList<>();
        User user = userRepository.findByEmail(username).orElse(null);
        
        if (user != null && user.getFavoriteSongs() != null) {
            for (Song song : user.getFavoriteSongs()) {
                if (song.getId() != null) {
                    ids.add(song.getId());
                }
            }
        }
        return ids;
    }
}