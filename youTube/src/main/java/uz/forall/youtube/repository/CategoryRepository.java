package uz.forall.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.youtube.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
