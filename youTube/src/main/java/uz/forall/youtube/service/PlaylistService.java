package uz.forall.youtube.service;

import org.springframework.stereotype.Service;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.entity.Playlist;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.payload.PlaylistDto;
import uz.forall.youtube.payload.PlaylistsFromCategory;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final CategoryRepository categoryRepository;

    public PlaylistService(PlaylistRepository playlistRepository, CategoryRepository categoryRepository) {
        this.playlistRepository = playlistRepository;
        this.categoryRepository = categoryRepository;
    }

    public ApiResult getPlayList() {
        List<Playlist> playlistList = playlistRepository.findAll();
        return new ApiResult("Playlistlar ro'yxati", true, playlistList);
    }

    public ApiResult getPlaylistsFromCategory(Long categoryId) {
        List<Playlist> allByCategoryId = categoryRepository.findAllByCategoryId(categoryId);
        List<PlaylistsFromCategory> playlistDtos = new ArrayList<>();
        for (Playlist playlist : playlistRepository.findAll()) {
            boolean active = false;
            if (allByCategoryId.contains(playlist))
                active = true;
            playlistDtos.add(new PlaylistsFromCategory(playlist.getId(),playlist.getName(), active));
        }
        return new ApiResult("Playlistlar ro'yxati", true, playlistDtos);
    }

}
