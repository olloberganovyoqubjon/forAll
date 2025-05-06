package uz.ish.lexuz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LexUzApplication {

    public static void main(String[] args) {
        SpringApplication.run(LexUzApplication.class, args);
    }

}
