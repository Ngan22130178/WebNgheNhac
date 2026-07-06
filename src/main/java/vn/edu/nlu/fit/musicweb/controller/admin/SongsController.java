package vn.edu.nlu.fit.musicweb.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.repository.SongRepository;
import vn.edu.nlu.fit.musicweb.service.SongService;


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
}
