package uz.forall.youtube.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.entity.Video;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.payload.VideoDto;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.repository.VideoRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    String folderPath = "E:/videos";

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;

    public VideoService(VideoRepository videoRepository, CategoryRepository categoryRepository) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
    }

    public ApiResult analyzeVideo(Long categoryId) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));

            if (files != null && files.length > 0) {
                System.out.println("MP4 fayllar ro'yxati:");
                for (File file : files) {
                    Optional<Video> optionalVideo = videoRepository.findVideoByTitle(file.getName());
                    if (optionalVideo.isEmpty()) {
                        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
                        if (optionalCategory.isEmpty()) return new ApiResult("Kategoriya topilmadi", false);
                        videoRepository.save(new Video(null, file.getName(), optionalCategory.get()));
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

    public ApiResult getVideos(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videoPage;
        if (categoryId == 0)
            videoPage = videoRepository.findAll(pageable);
        else {
            videoPage = videoRepository.findAllByCategory_Id(categoryId, pageable);
        }

        // DTO lar uchun bo'sh ro'yxat yaratamiz
        List<VideoDto> videoDtoList = new ArrayList<>();

        if (!videoPage.isEmpty()) {
            for (Video video : videoPage.getContent()) {
                String title = video.getTitle().substring(0, video.getTitle().length() - 4);
                title = title + ".webp";
                Path path = Paths.get(folderPath).resolve(title).normalize();

                String base64String = "";
                if (Files.exists(path)) { // Fayl mavjudligini tekshirish
                    try {
                        byte[] imageBytes = Files.readAllBytes(path);
                        base64String = Base64.getEncoder().encodeToString(imageBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // DTO ni yaratamiz va ro'yxatga qo'shamiz
                VideoDto videoDto = new VideoDto(video.getId(), video.getTitle(), base64String);
                videoDtoList.add(videoDto);
            }
        }

        // Yangi sahifalanadigan Page<VideoDto> yaratamiz
        Page<VideoDto> videoDtos = new PageImpl<>(videoDtoList, pageable, videoPage.getTotalElements());

        return new ApiResult("Video ro'yxati", true, videoDtos);
    }

    public ResponseEntity<Resource> getVideo(Long id, String rangeHeader) {
        try {
            Optional<Video> optionalVideo = videoRepository.findById(id);
            if (optionalVideo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String fileName = optionalVideo.get().getTitle();
            Path filePath = Paths.get(folderPath).resolve(fileName).normalize();
            File file = filePath.toFile();

            if (!file.exists() || !file.canRead()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            long fileSize = file.length();
            long rangeStart = 0;
            long rangeEnd = fileSize - 1;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.substring(6).split("-");
                try {
                    rangeStart = Long.parseLong(ranges[0]);
                    if (ranges.length > 1 && !ranges[1].isEmpty()) {
                        rangeEnd = Long.parseLong(ranges[1]);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
                }
            }

            if (rangeEnd >= fileSize) {
                rangeEnd = fileSize - 1;
            }

            long contentLength = rangeEnd - rangeStart + 1;
            InputStream inputStream = new FileInputStream(file);
            inputStream.skip(rangeStart);
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
            headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
