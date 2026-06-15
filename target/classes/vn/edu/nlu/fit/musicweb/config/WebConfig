package vn.edu.nlu.fit.musicweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ audio
        registry.addResourceHandler("/uploads/audio/**")
                .addResourceLocations("file:D:/music_data/audio/");
        
        // Ánh xạ cover
        registry.addResourceHandler("/uploads/cover/**")
                .addResourceLocations("file:D:/music_data/cover/");
        
        // Ánh xạ lyric (Nếu bạn vẫn lưu file .lrc riêng biệt)
        registry.addResourceHandler("/uploads/lyric/**")
                .addResourceLocations("file:D:/music_data/lyric/");
    }
}