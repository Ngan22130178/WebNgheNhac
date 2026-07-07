package vn.edu.nlu.fit.musicweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.nlu.fit.musicweb.model.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm kiếm cơ bản
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findById(Long id);
    // Các phương thức hỗ trợ cho Admin:
    
    // Tìm tất cả người dùng theo vai trò (VD: tìm danh sách User thường)
    List<User> findByRole(String role);
    
    // Tìm danh sách người dùng theo trạng thái hoạt động (enabled)
    List<User> findByEnabled(boolean enabled);
    
    // Tìm kiếm người dùng theo tên (hỗ trợ chức năng tìm kiếm trong trang quản lý)
    List<User> findByFullNameContainingIgnoreCase(String fullName);
    
    // Kiểm tra email đã tồn tại hay chưa (hữu ích khi thêm mới người dùng)
    boolean existsByEmail(String email);
}