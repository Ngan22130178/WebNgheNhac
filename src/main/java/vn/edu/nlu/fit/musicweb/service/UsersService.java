package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Hàm đổi mật khẩu bảo mật
     */
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }

        // Kiểm tra xem mật khẩu hiện tại có khớp với mật khẩu cũ trong DB không
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        // TIẾN HÀNH MÃ HÓA MẬT KHẨU MỚI TRƯỚC KHI LƯU
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return true;
    }
}
