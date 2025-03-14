package uz.forall.youtube.helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static uz.forall.youtube.helper.SeeTheTime.FOLDER_PATH;


public class WebPToPngConverter {
    public static void save(String fileName) {
        try {
            // WebP faylini ochish
            Path path = Paths.get(FOLDER_PATH).resolve(fileName).normalize();
            File input = new File(path.toString());
            BufferedImage image = ImageIO.read(input);

            // PNG formatiga o'tkazish va saqlash
            String outputImagePath = FOLDER_PATH + fileName.substring(0, fileName.length() - 4) + ".png";
            File output = new File(outputImagePath);
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            System.err.println("Xatolik: " + e.getMessage());
        }
    }
}

