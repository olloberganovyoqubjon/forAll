package uz.forall.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.youtube.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
