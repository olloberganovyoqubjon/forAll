package uz.forall.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.File;

@SpringBootApplication
@EnableDiscoveryClient
public class LibraryApplication {
    public static String uploadDir = "files/library";
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Agar uploads papkasi mavjud bo'lmasa, uni yaratamiz
        }
    }

}
