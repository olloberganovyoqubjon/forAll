package uz.forall.youtube.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.service.VideoService;

@RestController
@RequestMapping
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("analyze/{categoryId}")
    public HttpEntity<?> analyzeVideo(@PathVariable Long categoryId) {
        ApiResult apiResult = videoService.analyzeVideo(categoryId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("videos/{categoryId}")
    public HttpEntity<?> getVideos(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ApiResult apiResult = videoService.getVideos(categoryId,page,size);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
    @GetMapping(value = "video/{id}", produces = "video/mp4")
    public HttpEntity<?> getVideo(@PathVariable Long id, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        return videoService.getVideo(id, rangeHeader);
    }
}
