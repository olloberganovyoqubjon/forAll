package uz.forall.appstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.File;

@SpringBootApplication
@EnableDiscoveryClient
public class AppStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppStoreApplication.class, args);
        String uploadDir = "files/appstore";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Agar uploads papkasi mavjud bo'lmasa, uni yaratamiz
        }
    }

}
