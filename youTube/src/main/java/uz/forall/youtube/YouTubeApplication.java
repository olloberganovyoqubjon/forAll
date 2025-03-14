package uz.forall.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import uz.forall.youtube.helper.ConfigReader;

@SpringBootApplication
@EnableScheduling
public class YouTubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouTubeApplication.class, args);
	}

}
