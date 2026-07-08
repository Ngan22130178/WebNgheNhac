package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
@Service
public class MusicScannerService {

@Autowired
    private SongRepository songRepository;

    private static final String FOLDER_PATH = "D:/music-upload/audio/";

    @Transactional
    public void scanAndSync() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) return;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (files == null) return;

        // 1. Xóa các bản ghi không còn file
        deleteMissingSongs(files);

        // 2. Thêm các file mới
        addNewSongs(files);
    }

    private void deleteMissingSongs(File[] existingFiles) {
        // Tạo Set các file hiện có trên ổ đĩa để tra cứu O(1)
        Set<String> filesOnDisk = Arrays.stream(existingFiles)
                                        .map(f -> "/audio/" + f.getName())
                                        .collect(Collectors.toSet());

        List<Song> allSongs = songRepository.findAll();
        for (Song song : allSongs) {
            if (!filesOnDisk.contains(song.getUrl())) {
                songRepository.delete(song);
            }
        }
    }

    private void addNewSongs(File[] existingFiles) {
        // Lấy danh sách URL đang có trong DB
        Set<String> existingUrlsInDb = songRepository.findAll().stream()
                                                     .map(Song::getUrl)
                                                     .collect(Collectors.toSet());

        for (File file : existingFiles) {
            String fileUrl = "/audio/" + file.getName();

            if (!existingUrlsInDb.contains(fileUrl)) {
                Song newSong = parseFileToSong(file);
                songRepository.save(newSong);
            }
        }
    }

    // Tách riêng logic parsing để code gọn hơn
    private Song parseFileToSong(File file) {
        String fileName = file.getName();
        String nameWithoutTimestamp = fileName;

        // Xử lý logic tách timestamp
        if (fileName.contains("_")) {
            String prefix = fileName.substring(0, fileName.indexOf("_"));
            if (prefix.matches("\\d+")) {
                nameWithoutTimestamp = fileName.substring(fileName.indexOf("_") + 1);
            }
        }

        int lastDotIndex = nameWithoutTimestamp.lastIndexOf(".");
        String nameOnly = (lastDotIndex != -1) ? nameWithoutTimestamp.substring(0, lastDotIndex) : nameWithoutTimestamp;

        String title = nameOnly;
        String artist = "Unknown";

        int dashIndex = nameOnly.indexOf("-");
        if (dashIndex != -1) {
            title = nameOnly.substring(0, dashIndex).trim();
            artist = nameOnly.substring(dashIndex + 1).trim();
        }

        return Song.builder()
                .title(title)
                .artist(artist)
                .url("/audio/" + fileName)
                .build();
    }
}