package vn.edu.nlu.fit.musicweb.service;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;
import vn.edu.nlu.fit.musicweb.security.CustomUserDetails;

import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + email));
        System.out.println("DEBUG: User email: " + user.getEmail());
        System.out.println("DEBUG: Password in DB: " + user.getPassword());
        // Cần tạo danh sách quyền (Authorities)
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        // TRẢ VỀ CUSTOM USER DETAILS CỦA BẠN
        return new CustomUserDetails(
            user.getEmail(),
            user.getPassword(),
            authorities,
            user.getFullName(), // Lấy từ Model User của bạn
            user.getAvatarUrl() // Lấy từ Model User của bạn
        );
    }
}