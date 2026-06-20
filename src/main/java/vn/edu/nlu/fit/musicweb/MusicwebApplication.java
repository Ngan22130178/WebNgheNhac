package vn.edu.nlu.fit.musicweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "vn.edu.nlu.fit.musicweb")
public class MusicwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicwebApplication.class, args);
    }
}