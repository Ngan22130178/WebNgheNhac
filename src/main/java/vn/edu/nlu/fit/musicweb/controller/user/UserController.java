package vn.edu.nlu.fit.musicweb.controller.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.service.SongService;
import vn.edu.nlu.fit.musicweb.service.UsersService;

@Controller
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private SongService songService;

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal Object principal, Model model) {
        if (principal == null || principal.equals("anonymousUser")) {
            return "redirect:/login";
        }
        model.addAttribute("user", principal);
        return "profile";
    }

    @GetMapping("/api/user/personal-songs")
    public String getPersonalSongs(Model model) {
        return "profile_playlist";
    }

    // 1. SỬA CHO PLAYLIST CỦA TÔI
    @GetMapping("/api/user/playlists")
    public String getUserPlaylists(Model model, java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();

        // Lấy danh sách bài hát (Vì dùng chung nên mình gọi hàm lấy bài hát yêu thích/playlist sẵn có của bạn)
        List<Song> playlistSongs = songService.getFavoriteSongsByUser(username);

        model.addAttribute("songs", playlistSongs);
        model.addAttribute("isProfilePage", true); // Bật cờ hiện nút Thùng rác

        return "fragments/songs_table";
    }

    // 2. SỬA CHO ALBUM ĐÃ LƯU (GỌI Y HỆT PLAYLIST ĐỂ ĐỒNG BỘ 100%)
    @GetMapping("/api/user/albums")
    public String getUserAlbums(Model model, java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();

        // Gọi chung một hàm dịch vụ với Playlist để đảm bảo hễ Playlist đổi là Album đổi theo
        List<Song> albumSongs = songService.getFavoriteSongsByUser(username);

        model.addAttribute("songs", albumSongs);
        model.addAttribute("isProfilePage", true); // Bật cờ hiện nút Thùng rác

        return "fragments/songs_table";
    }

    @GetMapping("/api/user/favorite-songs")
    public String getFavoriteSongs(Model model, Principal principal) {
        // 1. Lấy thông tin username/userId người dùng đang đăng nhập hiện tại
        String username = principal.getName();

        // 2. Gọi Service lấy danh sách bài hát yêu thích của user này từ database
        List<Song> favSongs = songService.getFavoriteSongsByUser(username);

        // 3. Đưa danh sách bài hát vào model để gửi sang giao diện
        model.addAttribute("songs", favSongs);

        model.addAttribute("isProfilePage", true);
        // 4. Trả về đúng file fragment chứa bảng danh sách bài hát (không chứa
        // header/footer)
        // Ví dụ file đó của bạn nằm ở: WEB-INF/views/fragments/songs_table.jsp (hoặc
        // tương tự)
        return "fragments/songs_table";
    }

   @org.springframework.web.bind.annotation.ResponseBody
@PostMapping("/api/user/favorite/toggle")
public org.springframework.http.ResponseEntity<String> toggleFavoriteSong(
        @RequestParam("songId") Long songId,
        java.security.Principal principal) {

    if (principal == null) {
        // Trả về header ép trình duyệt chuyển hướng nếu chưa login
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("HX-Redirect", "/login");
        return new org.springframework.http.ResponseEntity<>(headers, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    String username = principal.getName();
    // Gọi DB để thêm/xóa bài hát yêu thích
    boolean isLiked = songService.toggleFavorite(username, songId);

    // Bắn trạng thái về cho HTMX thông qua Header custom tự chế hoặc trả về chuỗi text công nhận
    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
    // Trả về kết quả true/false để FE biết đường đổi màu trái tim
    headers.add("X-Liked-Status", String.valueOf(isLiked)); 

    return new org.springframework.http.ResponseEntity<>("", headers, org.springframework.http.HttpStatus.OK);
}

    @GetMapping("/api/user/account")
    public String getAccountSettings(@AuthenticationPrincipal Object principal, Model model) {
        if (principal == null || principal.equals("anonymousUser")) {
            return "fragments/account";
        }
        model.addAttribute("user", principal);
        return "fragments/account";
    }

    // ==========================================
    // 6. API Xử lý Lưu thông tin khi User bấm nút (POST)
    // ==========================================
    @PostMapping("/api/user/update-profile")
    public String updateProfile(
            @AuthenticationPrincipal Object principal,
            @RequestParam("displayName") String displayName,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            Model model) {

        if (principal == null || principal.equals("anonymousUser")) {
            return "<html><body>Lỗi xác thực!</body></html>";
        }

        // --- 1. LẤY EMAIL AN TOÀN TỪ SPRING SECURITY PRINCIPAL ---
        String userEmail = "";
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            userEmail = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else if (principal instanceof java.security.Principal) {
            userEmail = ((java.security.Principal) principal).getName();
        } else {
            userEmail = principal.toString();
        }

        // --- 2. TÌM USER THẬT TRONG DB BẰNG EMAIL ---
        User currentUser = usersService.getUserByEmail(userEmail);

        if (currentUser == null) {
            model.addAttribute("errorMessage", "Không tìm thấy tài khoản tương ứng trong cơ sở dữ liệu!");
            model.addAttribute("user", principal);
            return "fragments/account";
        }

        // Lấy ID từ entity User để chuẩn bị cho việc đổi mật khẩu
        Long userId = currentUser.getId();

        // --- 3. TIẾN HÀNH CẬP NHẬT TÊN HIỂN THỊ ---
        currentUser.setFullName(displayName);

        // --- 4. XỬ LÝ ĐỔI MẬT KHẨU ---
        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Mật khẩu mới và xác nhận không trùng khớp!");
                model.addAttribute("user", principal);
                return "fragments/account";
            }

            // Gọi hàm đổi mật khẩu bảo mật trong UsersService của bạn
            boolean isPasswordChanged = usersService.changePassword(userId, currentPassword, newPassword);

            if (!isPasswordChanged) {
                model.addAttribute("errorMessage", "Mật khẩu hiện tại không chính xác!");
                model.addAttribute("user", principal);
                return "fragments/account";
            }
        } else {
            // Nếu không đổi mật khẩu, chỉ lưu tên hiển thị mới bằng hàm saveUser đã có của
            // bạn
            usersService.saveUser(currentUser);
        }

        // --- 5. ĐỒNG BỘ LẠI TÊN MỚI VÀO SESSION ĐỂ HIỂN THỊ TRÊN AVATAR ---
        try {
            // Sử dụng Reflection để cập nhật động trường fullName trong session object nếu
            // class của bạn hỗ trợ
            java.lang.reflect.Method setFullNameMethod = principal.getClass().getMethod("setFullName", String.class);
            setFullNameMethod.invoke(principal, displayName);
        } catch (Exception e) {
            // Nếu Object Principal là class mặc định không có trường setFullName thì bỏ qua
            // bước này, DB đã lưu thành công
            System.out.println("Không thể set trực tiếp tên vào Principal Session: " + e.getMessage());
        }

        // Trả về dữ liệu thành công cho HTMX render lại vùng giao diện
        model.addAttribute("user", principal);
        model.addAttribute("successMessage", "Cập nhật thông tin thành công!");

        return "fragments/account";
    }
}