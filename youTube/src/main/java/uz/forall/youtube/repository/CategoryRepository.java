package uz.forall.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.entity.Playlist;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query(value = "select * " +
            "from playlist p " +
            "where p.id in (select p.id " +
            "               from playlist p " +
            "                        join video v on p.id = v.playlist_id " +
            "                        join category c on c.id = v.category_id " +
            "               where c.id = :categoryId " +
            "               group by p.id)", nativeQuery = true)
    List<Playlist>findAllByCategoryId(@Param("categoryId") Long categoryId);
}
