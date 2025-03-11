package uz.forall.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.youtube.entity.Video;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findVideoByTitle(String title);

    Page<Video> findAll(Pageable pageable);

    Page<Video> findAllByCategory_Id(Long idCategory, Pageable pageable);
}
