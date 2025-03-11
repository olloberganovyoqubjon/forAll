package uz.forall.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.forall.youtube.helper.ConfigReader;

@SpringBootApplication
public class YouTubeApplication {

	public static String folderPath = "";

	public static void main(String[] args) {
		folderPath = ConfigReader.readConfigFileToString("folder");
		SpringApplication.run(YouTubeApplication.class, args);


	}

}
