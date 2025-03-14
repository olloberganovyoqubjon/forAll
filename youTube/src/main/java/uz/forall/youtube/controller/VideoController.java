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

    @GetMapping("video/playlist/{playlistId}")
    public HttpEntity<?> getVideosPlaylist(
            @PathVariable Long playlistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ApiResult apiResult = videoService.getVideosPlaylist(playlistId,page,size);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("video/search/{search}")
    public HttpEntity<?> searchVideo(
            @PathVariable String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ApiResult apiResult = videoService.searchVideo(search, page, size);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
