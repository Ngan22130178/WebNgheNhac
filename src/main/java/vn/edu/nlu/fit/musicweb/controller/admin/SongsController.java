package vn.edu.nlu.fit.musicweb.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.service.SongService;
import java.io.File;
import java.nio.file.Path;
@Controller
@RequestMapping("/admin/songs") // Ánh xạ tới /admin/songs
public class SongsController {
    @Autowired
    private SongService songService;

    private final SongRepository songRepository;

    public SongsController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        // Lấy danh sách từ database
        model.addAttribute("songs", songRepository.findAll());
        // Trả về file JSP (đảm bảo file nằm ở /WEB-INF/views/admin.jsp)
        return "admin/songs"; // webapp/WEB-INF/views/admin/songs.jsp
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return "redirect:/admin/songs?error=empty";
        }

        for (MultipartFile file : files) {
            try {
                processAndSaveFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                // Bạn có thể log lỗi hoặc bỏ qua file lỗi để tiếp tục upload các file khác
            }
        }
        
        return "redirect:/admin/songs?success=true";
    }

    // Thêm hoặc Sửa (Lưu)
    @PostMapping("/save/{id}")
    @ResponseBody 
    public String saveSong(@PathVariable Long id, @ModelAttribute Song song) {
        // 1. Tìm bản ghi cũ trong DB
        Song existingSong = songService.getSongById(id);
        if (existingSong != null) {
            // 2. Cập nhật các trường từ form vào bản ghi cũ
            existingSong.setTitle(song.getTitle());
            existingSong.setArtist(song.getArtist());
            existingSong.setUrl(song.getUrl()); // Đảm bảo URL này nhận được giá trị mới từ form
            existingSong.setGenre(song.getGenre());
            existingSong.setAlbumName(song.getAlbumName());
            
            // 3. Lưu lại
            songService.saveSong(existingSong);
            return "success";
        }
        return "error";
    }

    // Xóa
    @PostMapping("/delete/{id}")
    @ResponseBody // Trả về kết quả trực tiếp cho AJAX
    public String deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return "success"; 
    }

    private void processAndSaveFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || file.isEmpty()) return;

        // 1. Phân loại
        String subDir;
        if (originalFileName.endsWith(".mp3")) {
            subDir = "audio/";
        } else if (originalFileName.endsWith(".txt") || originalFileName.endsWith(".lrc")) {
            subDir = "lyrics/";
        } else {
            return; // Bỏ qua file không đúng định dạng
        }

        // 2. Ghi file
        String safeFileName = System.currentTimeMillis() + "_" + originalFileName;
        String baseDir = "D:/music-upload/";
        File uploadFolder = new File(baseDir + subDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        Path path = Paths.get(uploadFolder.getAbsolutePath() + File.separator + safeFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 3. Lưu vào Database (Logic riêng cho từng loại)
        if (subDir.equals("audio/")) {
            Song newSong = Song.builder()
                    .title(originalFileName.replace(".mp3", ""))
                    .url("/audio/" + safeFileName)
                    .build();
            songService.saveSong(newSong);
        } 
        // Nếu là lyrics, bạn có thể gọi songService.updateLyrics(...)
    }
}
