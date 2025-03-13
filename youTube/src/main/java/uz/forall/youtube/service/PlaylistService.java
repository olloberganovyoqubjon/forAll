package uz.forall.youtube.service;

import org.springframework.stereotype.Service;
import uz.forall.youtube.entity.Playlist;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.repository.PlaylistRepository;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public ApiResult getPlayList() {
        List<Playlist> playlistList = playlistRepository.findAll();
        return new ApiResult("Playlistlar ro'yxati", true, playlistList);
    }
}
