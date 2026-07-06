package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import java.io.File;
import java.util.List;

@Service
public class MusicScannerService {

    @Autowired
    private SongRepository songRepository;

    @Transactional // Thêm vào đây để toàn bộ quá trình quét là một giao dịch
    public void scanAndSync() {
        String folderPath = "D:/music-upload/audio/";
        File folder = new File(folderPath);
        
        // Kiểm tra thư mục tồn tại
        if (!folder.exists()) return; 

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (files == null) return;

        // 1. Xóa các bản ghi trong DB mà file không còn tồn tại trên ổ D
        List<Song> allSongs = songRepository.findAll();
        for (Song song : allSongs) {
            String fileName = song.getUrl().replace("/audio/", "");
            File fileOnDisk = new File(folderPath + fileName);
            if (!fileOnDisk.exists()) {
                songRepository.delete(song); // Dùng delete(entity) sẽ an toàn hơn deleteByUrl
            }
        }

        // 2. Thêm các file mới trên ổ D vào DB
        List<String> existingUrls = songRepository.findAll().stream()
                                                .map(Song::getUrl).toList();

        for (File file : files) {
            String fileName = file.getName(); // Ví dụ: 1783348989238_Ánh Nắng Của Anh - Ca Sĩ A.mp3
            String fileUrl = "/audio/" + fileName;

            if (!existingUrls.contains(fileUrl)) {
                // --- XỬ LÝ TÁCH TÊN BÀI HÁT VÀ NGHỆ SĨ ---
                
                // 1. Bỏ timestamp: lấy chuỗi sau dấu "_"
                String nameWithoutTimestamp = fileName.contains("_") ? 
                                            fileName.substring(fileName.indexOf("_") + 1) : fileName;
                
                // 2. Bỏ đuôi file: lấy chuỗi trước dấu "." cuối cùng
                int lastDotIndex = nameWithoutTimestamp.lastIndexOf(".");
                String nameOnly = (lastDotIndex != -1) ? nameWithoutTimestamp.substring(0, lastDotIndex) : nameWithoutTimestamp;

                String title = nameOnly;
                String artist = "Unknown"; // Mặc định

                // 3. Tách Title - Artist dựa trên dấu "-"
                // Dùng indexOf để tìm dấu gạch ngang đầu tiên
                int dashIndex = nameOnly.indexOf("-");
                if (dashIndex != -1) {
                    title = nameOnly.substring(0, dashIndex).trim();
                    artist = nameOnly.substring(dashIndex + 1).trim();
                }

                // --- LƯU VÀO DATABASE ---
                Song newSong = Song.builder()
                        .title(title)
                        .artist(artist) // Đảm bảo Entity Song của bạn có trường artist
                        .url(fileUrl)
                        .build();
                
                songRepository.save(newSong);
            }
        }
    }
}