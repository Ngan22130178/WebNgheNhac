package vn.edu.nlu.fit.musicweb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.nlu.fit.musicweb.service.MusicScannerService;

@Configuration
public class AppStartupRunner {

    @Bean
    CommandLineRunner initScanner(MusicScannerService scanner) {
        return args -> {
            System.out.println("Đang bắt đầu quét ổ đĩa D để đồng bộ dữ liệu...");
            scanner.scanAndSync();
            System.out.println("Đã đồng bộ xong!");
        };
    }
}