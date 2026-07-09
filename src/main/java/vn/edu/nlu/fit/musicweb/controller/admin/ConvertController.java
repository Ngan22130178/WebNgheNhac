package vn.edu.nlu.fit.musicweb.controller.admin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import java.nio.file.Path;
import java.util.Map;
import java.io.File;

@Controller
@RequestMapping("/admin/convert")
public class ConvertController {

    private final String STORAGE_PATH = "D:/music-upload/audio/";

    @GetMapping
    public String index(Model model) {
        // Thiết lập để menu "Convert" sáng lên
        model.addAttribute("currentPage", "convert");
        return "admin/convert"; // Trả về trang convert.jsp
    }

    @PostMapping("/process")
    @ResponseBody // Trả về JSON để JS xử lý Toast
    public ResponseEntity<Map<String, String>> convertMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, String> response = new HashMap<>();
        try {
            File directory = new File(STORAGE_PATH);
            if (!directory.exists()) directory.mkdirs();

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
                Path videoPath = Paths.get(STORAGE_PATH + "temp_" + originalName);
                Files.copy(file.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);

                Path mp3Path = Paths.get(STORAGE_PATH + baseName + ".mp3");

                ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y", "-i", videoPath.toString(), "-vn", "-acodec", "libmp3lame", "-q:a", "2", mp3Path.toString()
                );
                pb.redirectErrorStream(true);
                Process process = pb.start();
                process.waitFor();

                Files.deleteIfExists(videoPath);
            }
            response.put("status", "success");
            response.put("message", "Đã chuyển đổi thành công " + files.length + " file!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
