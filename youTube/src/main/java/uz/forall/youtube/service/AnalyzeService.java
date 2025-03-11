package uz.forall.youtube.service;

import org.springframework.stereotype.Service;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.entity.Video;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.repository.VideoRepository;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static uz.forall.youtube.YouTubeApplication.folderPath;

@Service
public class AnalyzeService {

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;

    public AnalyzeService(VideoRepository videoRepository, CategoryRepository categoryRepository) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
    }


    public ApiResult analyzeVideoWithImage(String categoryName) {
        Path path = Paths.get(folderPath).resolve(categoryName).normalize();
        File folder = new File(path.toString());
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));

            if (files != null && files.length > 0) {
                for (File file : files) {
                    Optional<Video> optionalVideo = videoRepository.findVideoByTitle(file.getName());
                    if (optionalVideo.isEmpty()) {
                        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
                        if (optionalCategory.isEmpty()) return new ApiResult("Kategoriya topilmadi", false);
                        videoRepository.save(new Video(null, file.getName(), optionalCategory.get(),null));
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





    public ApiResult analyzeVideo(String folderPath, String categoryName, String playlistName) {

        return null;
    }
}
