package vn.edu.nlu.fit.musicweb.controller.admin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import java.nio.file.Path;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String convertMultipleFiles(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttributes) {
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                // 1. Lưu tạm file video
                String originalName = file.getOriginalFilename();
                String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
                Path videoPath = Paths.get(STORAGE_PATH + "temp_" + originalName);
                Files.copy(file.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);

                // 2. Đường dẫn output MP3
                String mp3FileName = baseName + ".mp3";
                Path mp3Path = Paths.get(STORAGE_PATH + mp3FileName);

                // 3. Gọi FFmpeg
                ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-i", videoPath.toString(), "-vn", "-acodec", "libmp3lame", "-q:a", "2", mp3Path.toString()
                );
                Process process = pb.start();
                process.waitFor();

                // 4. Xóa file video tạm
                Files.deleteIfExists(videoPath);
            }
            redirectAttributes.addFlashAttribute("message", "Đã chuyển đổi thành công " + files.length + " file!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/convert/";
    }
}
