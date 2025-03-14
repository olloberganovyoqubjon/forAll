package uz.forall.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.forall.youtube.entity.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findVideoByTitle(String title);

    List<Video> findAllVideosByCategory_Id(Long idCategory);

    Page<Video> findAll(Pageable pageable);

    @Query(value = "select v.id, v.title, v.category_id, v.playlist_id " +
            "from video v " +
            "where v.category_id = :idCategory and id in (select min(id) as id " +
            "             from video v " +
            "             where v.playlist_id IS NOT NULL " +
            "             group by playlist_id " +
            "             union " +
            "             select v.id as id " +
            "             from video v " +
            "             where v.playlist_id is null) " +
            "order by id desc", nativeQuery = true)
    List<Video> findAllByCategory_Id(@Param("idCategory") Long idCategory, Pageable pageable);


    @Query(value = "select v.id,v.title, v.category_id, v.playlist_id " +
            "from video v " +
            "where v.category_id NOT IN :blockedCategories " +
            "  and id in (select min(id) as id " +
            "             from video v " +
            "             where v.playlist_id IS NOT NULL " +
            "             group by playlist_id " +
            "             union " +
            "             select v.id as id " +
            "             from video v" +
            "             where v.playlist_id is null)" +
            "order by id desc", nativeQuery = true)
    List<Video> findAllByNotBlockedWithPlaylist(@Param("blockedCategories") List<Long> blockedCategories, Pageable pageable);

    @Query(value = "select v.id,v.title, v.category_id, v.playlist_id " +
            "from video v " +
            "where id in (select min(id) as id " +
            "             from video v " +
            "             where v.playlist_id IS NOT NULL " +
            "             group by playlist_id " +
            "             union " +
            "             select v.id as id " +
            "             from video v" +
            "             where v.playlist_id is null)" +
            "order by id desc", nativeQuery = true)
    List<Video> findAllByPlaylist(Pageable pageable);


    Page<Video> findAllByPlaylist_Id(Long playlistId, Pageable pageable);

    Integer countByPlaylist_Id(Long playlistId);


    @Query(value = "SELECT v.id, v.title, v.category_id, v.playlist_id " +
            "FROM video v " +
            "WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "  AND v.category_id NOT IN (:blockedCategories) " +
            "  AND v.id IN ( " +
            "    SELECT MIN(id) " +
            "    FROM video " +
            "    WHERE playlist_id IS NOT NULL " +
            "    GROUP BY playlist_id " +
            "    UNION " +
            "    SELECT id " +
            "    FROM video " +
            "    WHERE playlist_id IS NULL " +
            ") " +
            "ORDER BY v.id DESC ", nativeQuery = true)
    List<Video> findAllByTitleContainingAndBlocked(String search);

    List<Video> findAllByTitleContainingIgnoreCase(String search);
}
