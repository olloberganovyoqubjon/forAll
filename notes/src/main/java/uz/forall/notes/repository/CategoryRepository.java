package uz.forall.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.notes.eintity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);
    Optional<Category> findByNameAndUserId(String name, Long userId);
}
