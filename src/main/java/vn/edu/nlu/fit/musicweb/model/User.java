package vn.edu.nlu.fit.musicweb.model;

import jakarta.persistence.*; // Thư viện chuẩn để ánh xạ dữ liệu (JPA)
import lombok.*;              // Thư viện tự động sinh code (Getter, Setter, Constructor)
import java.util.List;

// @Entity: Đánh dấu class này là một bảng (Table) trong database
@Entity 
// @Table: Định nghĩa tên bảng cụ thể trong CSDL (vì "user" là từ khóa cấm trong SQL)
@Table(name = "users") 
// @Getter, @Setter: Tự sinh ra các phương thức lấy và gán giá trị (từ Lombok)
@Getter @Setter 
// @NoArgsConstructor: Tự tạo constructor không tham số (Bắt buộc phải có cho JPA)
@NoArgsConstructor 
// @AllArgsConstructor: Tự tạo constructor có tất cả tham số
@AllArgsConstructor 
// @Builder: Hỗ trợ tạo đối tượng theo kiểu "Builder pattern" (giúp code sạch hơn)
@Builder 
public class User {

    // @Id: Đánh dấu đây là khóa chính (Primary Key) của bảng
    @Id 
    // @GeneratedValue: Tự động tăng giá trị ID mỗi khi thêm mới (Auto Increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    // @Column: Định nghĩa các thuộc tính chi tiết của cột
    // nullable = false: Bắt buộc phải có dữ liệu (không được để trống)
    // unique = true: Giá trị này là duy nhất trong bảng (không được trùng)
    @Column(nullable = false, unique = true)
    private String email;

    private String password; // Để trống nếu user đăng nhập bằng Google

    private String googleId; // Lưu ID riêng biệt do Google cấp

    // role: Đánh dấu quyền hạn của user (Admin hoặc User thường)
    @Column(nullable = false)
    private String role;

    // Các thông tin cơ bản của hồ sơ người dùng
    private String fullName;
    private String avatarUrl;

    // @ElementCollection: Dùng để lưu danh sách các phần tử đơn giản (như List<String>)
    // JPA sẽ tự tạo một bảng phụ để lưu các ID bài hát yêu thích này và liên kết về bảng User
    @ElementCollection 
    private List<String> favoriteSongIds;

    // Thêm trường để biết User này dùng provider nào (local/google)
    @Column(nullable = true)
    private String provider= "LOCAL"; // "LOCAL" hoặc "GOOGLE"

    // Thêm trường để quản lý trạng thái tài khoản
    @Column(nullable = false)
    private boolean enabled = true;
}