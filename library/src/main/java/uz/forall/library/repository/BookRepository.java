package uz.forall.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.library.entity.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByCategoryId(UUID categoryId);
    List<Book> findByFileType(String fileType);
    List<Book> findByCategoryIdAndFileType(UUID categoryId, String fileType);
}
