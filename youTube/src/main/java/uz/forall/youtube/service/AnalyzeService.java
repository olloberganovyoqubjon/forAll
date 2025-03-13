package uz.forall.youtube.service;

import org.springframework.stereotype.Service;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.entity.Playlist;
import uz.forall.youtube.entity.Video;
import uz.forall.youtube.helper.VideoToImageConverter;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.repository.PlaylistRepository;
import uz.forall.youtube.repository.VideoRepository;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static uz.forall.youtube.YouTubeApplication.FOLDER_PATH;

@Service
public class AnalyzeService {

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final PlaylistRepository playlistRepository;

    public AnalyzeService(VideoRepository videoRepository, CategoryRepository categoryRepository, PlaylistRepository playlistRepository) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.playlistRepository = playlistRepository;
    }


    public ApiResult analyzeVideoWithImage(String categoryName, String otherFolderPath, Playlist playlist) {
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isEmpty()) return new ApiResult("Kategoriya topilmadi", false);
        Path path;
        if (otherFolderPath == null) {
            path = Paths.get(FOLDER_PATH).resolve(categoryName).normalize();
        } else {
            path = Paths.get(otherFolderPath).normalize();
        }
        File folder = new File(path.toString());
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));
            deleteNotExistsFromFolder(files, optionalCategory);
            if (files != null && files.length > 0) {
                for (File file : files) {
                    Optional<Video> optionalVideo = videoRepository.findVideoByTitle(file.getName());
                    if (optionalVideo.isEmpty()) {
                        videoRepository.save(new Video(null, file.getName(), optionalCategory.get(), playlist));
                    }
                }
            } else {
                return new ApiResult("MP4 fayllari mavjud emas", false);
            }
        } else {
            return new ApiResult("Bunday papka mavjud emas", false);
        }
        return new ApiResult("Video analiz qilindi", true);
    }


    public ApiResult analyzeVideo(String folderPath, String categoryName, Playlist playlist) {
        Path path = Paths.get(folderPath).normalize();
        File folder = new File(path.toString());
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
            if (optionalCategory.isEmpty()) {
                return new ApiResult("Kategoriya topilmadi", false);
            }
            deleteNotExistsFromFolder(files, optionalCategory);
            assert files != null;
            for (File file : files) {
                Optional<Video> optionalVideo = videoRepository.findVideoByTitle(file.getName());
                if (optionalVideo.isEmpty()) {
                    VideoToImageConverter.save(file.getName());
                    videoRepository.save(new Video(null, file.getName(), optionalCategory.get(), playlist));
                }
            }

        }
        return new ApiResult("Video analiz qilindi", true);
    }

    private ApiResult analyzePlaylist(String folderPath, String categoryName, String playlistName, Boolean isWebp){
        Optional<Playlist> optionalPlaylist = playlistRepository.findByName(playlistName);
        if (optionalPlaylist.isEmpty()) {
            return new ApiResult("Playlist topilmadi", false);
        }
        Playlist playlist = optionalPlaylist.get();
        if (isWebp){
            return analyzeVideoWithImage(categoryName, folderPath, playlist);
        } else {
            return analyzeVideo(folderPath,categoryName,playlist);
        }
    }


    private void deleteNotExistsFromFolder(File[] files, Optional<Category> optionalCategory) {
        Set<String> fileNames;
        if (files != null) {
            fileNames = Arrays.stream(files)
                    .map(File::getName) // Faqat fayl nomlarini olish
                    .collect(Collectors.toSet());
        } else {
            fileNames = new HashSet<>();
        }

        // Bazadan kategoriya bo‘yicha videolarni olish
        List<Video> allVideosByCategoryId = videoRepository.findAllVideosByCategory_Id(optionalCategory.get().getId());

        // Fayllar orasida yo'qligini tekshirib, bazadan o‘chirish
        List<Video> videosToDelete = allVideosByCategoryId.stream()
                .filter(video -> !fileNames.contains(video.getTitle())) // Agar fayl yo‘q bo‘lsa, o‘chirishga qo‘shish
                .collect(Collectors.toList());

        // 4. Bazadan o‘chirish
        if (!videosToDelete.isEmpty()) {
            videoRepository.deleteAll(videosToDelete);
        }
    }
}
