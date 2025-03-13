package uz.forall.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.youtube.entity.Playlist;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByName(String name);
}
