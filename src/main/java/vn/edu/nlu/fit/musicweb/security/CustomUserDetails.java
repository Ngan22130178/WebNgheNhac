package vn.edu.nlu.fit.musicweb.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private String email;
    private String password;
    private List<? extends GrantedAuthority> authorities;
    private String fullName;
    private String avatarUrl;

    public CustomUserDetails(String email, String password, List<? extends GrantedAuthority> authorities, String fullName, String avatarUrl) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return email; } // Trả về email thay vì username
    
    // Các phương thức bắt buộc khác của UserDetails
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // Getter cho các field bổ sung
    public String getFullName() { return fullName; }
    public String getAvatarUrl() { return avatarUrl; }
}