package uz.forall.youtube.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.service.PlaylistService;

@RequestMapping("playlist")
@RestController
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("all")
    public HttpEntity<?> getPlaylists() {
        ApiResult apiResult = playlistService.getPlayList();
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
