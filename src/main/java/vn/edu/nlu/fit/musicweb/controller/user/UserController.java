package vn.edu.nlu.fit.musicweb.controller.user;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import vn.edu.nlu.fit.musicweb.model.Song;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.service.SongService;
import vn.edu.nlu.fit.musicweb.service.UsersService;

@Controller
@RequestMapping("/user") 
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
        return "user/profile"; 
    }

    @GetMapping("/api/user/personal-songs")
    public String getPersonalSongs(Model model) {
        return "user/profile_playlist";
    }

    @GetMapping("/api/user/playlists")
    public String getUserPlaylists(Model model, java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();

        List<Song> playlistSongs = songService.getFavoriteSongsByUser(username);
        model.addAttribute("songs", playlistSongs);
        model.addAttribute("isProfilePage", true);
        model.addAttribute("pageType", "playlist");

        return "fragments/songs_table";
    }

    @GetMapping("/api/user/albums")
    public String getUserAlbums(Model model, java.security.Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();

        List<Song> albumSongs = songService.getFavoriteSongsByUser(username);
        model.addAttribute("songs", albumSongs);
        model.addAttribute("isProfilePage", true);
        model.addAttribute("pageType", "album");

        return "fragments/songs_table";
    }

    @GetMapping("/api/user/favorite-songs")
    public String getFavoriteSongs(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();

        List<Song> favSongs = songService.getFavoriteSongsByUser(username);
        model.addAttribute("songs", favSongs);
        model.addAttribute("isProfilePage", true);
        model.addAttribute("pageType", "favorite");

        return "fragments/songs_table";
    }

    @PostMapping("/api/user/favorite/toggle")
    public String toggleFavoriteSong(
            @RequestParam("songId") Long songId,
            java.security.Principal principal,
            HttpServletResponse response, 
            Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        boolean isLiked = songService.toggleFavorite(username, songId);

        Song currentSong = songService.getSongById(songId);
        model.addAttribute("song", currentSong);

        List<Long> favoriteSongIds = songService.getFavoriteSongIdsByUser(username);
        model.addAttribute("favoriteSongIds", favoriteSongIds);

        response.setHeader("HX-Trigger", "favoriteUpdated");

        return "user/favorite_button";
    }

    @GetMapping("/api/user/account")
    public String getAccountSettings(@AuthenticationPrincipal Object principal, Model model) {
        if (principal == null || principal.equals("anonymousUser")) {
            return "redirect:/login";
        }

        String userEmail = "";
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            userEmail = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else if (principal instanceof java.security.Principal) {
            userEmail = ((java.security.Principal) principal).getName();
        } else {
            userEmail = principal.toString();
        }

        User currentUser = usersService.getUserByEmail(userEmail);
        model.addAttribute("user", currentUser != null ? currentUser : principal);

        return "user/account";
    }

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

        String userEmail = "";
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            userEmail = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else if (principal instanceof java.security.Principal) {
            userEmail = ((java.security.Principal) principal).getName();
        } else {
            userEmail = principal.toString();
        }

        User currentUser = usersService.getUserByEmail(userEmail);

        if (currentUser == null) {
            model.addAttribute("errorMessage", "Không tìm thấy tài khoản trong cơ sở dữ liệu!");
            model.addAttribute("user", principal);
            return "user/account";
        }

        Long userId = currentUser.getId();
        currentUser.setFullName(displayName);

        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Mật khẩu mới và xác nhận không trùng khớp!");
                model.addAttribute("user", currentUser);
                return "user/account";
            }

            boolean isPasswordChanged = usersService.changePassword(userId, currentPassword, newPassword);

            if (!isPasswordChanged) {
                model.addAttribute("errorMessage", "Mật khẩu hiện tại không chính xác!");
                model.addAttribute("user", currentUser);
                return "user/account";
            }
        } else {
            usersService.saveUser(currentUser);
        }

        try {
            java.lang.reflect.Method setFullNameMethod = principal.getClass().getMethod("setFullName", String.class);
            setFullNameMethod.invoke(principal, displayName);
        } catch (Exception e) {
            System.out.println("Không thể cập nhật tên vào Session: " + e.getMessage());
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("successMessage", "Cập nhật thông tin thành công!");

        return "user/account";
    }
}