package uz.forall.youtube.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.entity.Playlist;
import uz.forall.youtube.entity.Video;
import uz.forall.youtube.helper.VideoToImageConverter;
import uz.forall.youtube.helper.WebPToPngConverter;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.repository.PlaylistRepository;
import uz.forall.youtube.repository.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static uz.forall.youtube.YouTubeApplication.FOLDER_PATH;

@Service
public class FileUploadService {

    private final CategoryRepository categoryRepository;
    private final PlaylistRepository playlistRepository;
    private final VideoRepository videoRepository;

    public FileUploadService(CategoryRepository categoryRepository, PlaylistRepository playlistRepository, VideoRepository videoRepository) {
        this.categoryRepository = categoryRepository;
        this.playlistRepository = playlistRepository;
        this.videoRepository = videoRepository;
    }


    public ApiResult saveVideo(MultipartFile file, String fileName, int chunkIndex, int totalChunks, String categoryName, String playlistName, Boolean webp) throws IOException {
        if (chunkIndex == 0) {
            Optional<Video> optionalVideo = videoRepository.findVideoByTitle(fileName);
            if (optionalVideo.isPresent()) {
                return new ApiResult("Bunday video mavjud, keyingi video", true);
            }
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
            if (optionalCategory.isEmpty()) {
                return new ApiResult("Bunday category mavjud emas!", false);
            }
            Category category = optionalCategory.get();
            Playlist playlist = null;
            if (playlistName != null) {
                Optional<Playlist> optionalPlaylist = playlistRepository.findByName(playlistName);
                playlist = optionalPlaylist.orElseGet(() -> playlistRepository.save(new Playlist(null, playlistName)));
            }
            videoRepository.save(new Video(null, fileName, category, playlist));
        }
        // Faylni serverga saqlash
        Path filePath = Paths.get(FOLDER_PATH).resolve(fileName + ".part" + chunkIndex);
        Files.write(filePath, file.getBytes());

        // Agar oxirgi bo‘lak yuklangan bo‘lsa, faylni birlashtiramiz
        if (chunkIndex == totalChunks - 1) {
            mergeChunks(fileName, totalChunks);
            if (fileName.endsWith(".webp"))
                WebPToPngConverter.save(fileName);
            else {
                if (!webp)
                    VideoToImageConverter.save(fileName);
            }
            System.out.println("Barcha bo‘laklar yuklandi: " + fileName);
        }

        return new ApiResult("Chunk " + chunkIndex + " received for " + fileName, true);
    }



    // Barcha bo‘laklarni birlashtirib, faylni tiklash
    private void mergeChunks(String fileName, int totalChunks) throws IOException {
        Path mergedFile = Paths.get(FOLDER_PATH).resolve(fileName);
        try (OutputStream outputStream = Files.newOutputStream(mergedFile)) {
            for (int i = 0; i < totalChunks; i++) {
                Path chunkPath = Paths.get(FOLDER_PATH).resolve(fileName + ".part" + i);
                Files.copy(chunkPath, outputStream);
                Files.delete(chunkPath); // Eski bo‘laklarni o‘chirish
            }
        }
    }

    public ApiResult saveWebp(MultipartFile webpFile, String fileName) {
        try {
            // Papkani yaratish (agar mavjud bo‘lmasa)
            File uploadDir = new File(FOLDER_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Fayl nomini .webp qo‘shimchasi bilan shakllantirish
            String webpFileName  = fileName.substring(0, fileName.length() - 4) + ".webp";
            Path filePath = Paths.get(FOLDER_PATH).resolve(webpFileName).normalize();

            // Faylni saqlash
            Files.write(filePath, webpFile.getBytes());

            return new ApiResult("Webp fayl muvaffaqiyatli yuklandi: " + webpFileName, true);

        } catch (IOException e) {
            // Xatolik bo‘lsa
            return new ApiResult("Webp faylni yuklashda xatolik:",false);
        }
    }
}
