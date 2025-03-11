package uz.forall.youtube.helper;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoToImageConverter {

    public static void save(String videoName, String path) {
        try {
            Path inputVideoPath = Paths.get(path).resolve(videoName).normalize();
            String outputImagePath = path + videoName.substring(0, videoName.length() - 4) + ".png";
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath.toString());
            grabber.start();

            // Birinchi freymni olish
            Frame frame = grabber.grabImage();
            if (frame != null) {
                // Java2D FrameConverter orqali BufferedImage ga o'tkazish
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.convert(frame);

                // Katalog mavjudligini tekshirish va yaratish
                File outputFile = new File(outputImagePath);
                outputFile.getParentFile().mkdirs(); // Agar papka mavjud bo'lmasa, yaratadi

                // PNG formatida saqlash
                ImageIO.write(bufferedImage, "png", outputFile);

                System.out.println("Rasm saqlandi: " + outputImagePath);
            }

            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
