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
import uz.forall.youtube.entity.Video;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.payload.VideoDto;
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

import static uz.forall.youtube.YouTubeApplication.FOLDER_PATH;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
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
        Page<VideoDto> videoDtos = getVidePage(videoPage, pageable);

        return new ApiResult("Video ro'yxati", true, videoDtos);
    }


    private Page<VideoDto> getVidePage(Page<Video> videoPage, Pageable pageable) {
        List<VideoDto> videoDtoList = new ArrayList<>();

        if (!videoPage.isEmpty()) {
            for (Video video : videoPage.getContent()) {
                String title = video.getTitle().substring(0, video.getTitle().length() - 4);
                String webpTitle = title + ".webp";
                Path webpPath = Paths.get(FOLDER_PATH).resolve(video.getCategory().getName()).resolve(webpTitle).normalize();


                String pngTitle = title + ".webp";
                Path pngPath = Paths.get(FOLDER_PATH).resolve(video.getCategory().getName()).resolve(pngTitle).normalize();

                boolean webp = false;
                String base64String = "";
                if (Files.exists(webpPath)) { // Fayl mavjudligini tekshirish
                    try {
                        byte[] imageBytes = Files.readAllBytes(webpPath);
                        base64String = Base64.getEncoder().encodeToString(imageBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    webp = true;
                } else if (Files.exists(pngPath)) { // Fayl mavjudligini tekshirish
                    try {
                        byte[] imageBytes = Files.readAllBytes(pngPath);
                        base64String = Base64.getEncoder().encodeToString(imageBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    webp = false;
                }
                // DTO ni yaratamiz va ro'yxatga qo'shamiz
                VideoDto videoDto = new VideoDto(video.getId(), video.getTitle(), base64String, webp);
                videoDtoList.add(videoDto);
            }
        }

        // Yangi sahifalanadigan Page<VideoDto> yaratamiz
        Page<VideoDto> videoDtos = new PageImpl<>(videoDtoList, pageable, videoPage.getTotalElements());
        return videoDtos;
    }

    public ResponseEntity<Resource> getVideo(Long id, String rangeHeader) {
        try {
            Optional<Video> optionalVideo = videoRepository.findById(id);
            if (optionalVideo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String fileName = optionalVideo.get().getTitle();
            Path filePath = Paths.get(FOLDER_PATH).resolve(optionalVideo.get().getCategory().getName()).resolve(fileName).normalize();
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

    public ApiResult getVideosPlaylist(Long playlistId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videoPage = videoRepository.findAllByPlaylist_Id(playlistId, pageable);
//
//        // DTO lar uchun bo'sh ro'yxat yaratamiz
//        List<VideoDto> videoDtoList = new ArrayList<>();
//
//        if (!videoPage.isEmpty()) {
//            for (Video video : videoPage.getContent()) {
//                String title = video.getTitle().substring(0, video.getTitle().length() - 4);
//                title = title + ".webp";
//                Path path = Paths.get(FOLDER_PATH).resolve(video.getCategory().getName()).resolve(title).normalize();
//
//                String base64String = "";
//                if (Files.exists(path)) { // Fayl mavjudligini tekshirish
//                    try {
//                        byte[] imageBytes = Files.readAllBytes(path);
//                        base64String = Base64.getEncoder().encodeToString(imageBytes);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // DTO ni yaratamiz va ro'yxatga qo'shamiz
//                VideoDto videoDto = new VideoDto(video.getId(), video.getTitle(), base64String);
//                videoDtoList.add(videoDto);
//            }
//        }
        Page<VideoDto> videoDtos = getVidePage(videoPage, pageable);
        // Yangi sahifalanadigan Page<VideoDto> yaratamiz
//        Page<VideoDto> videoDtos = new PageImpl<>(videoDtoList, pageable, videoPage.getTotalElements());

        return new ApiResult("Video ro'yxati", true, videoDtos);
    }
}
