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
    }

}
