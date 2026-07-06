package vn.edu.nlu.fit.musicweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ URL /audio/** sang ổ đĩa vật lý D:/music-upload/audio/
        registry.addResourceHandler("/audio/**")
                .addResourceLocations("file:D:/music-upload/audio/");
     
    }
}