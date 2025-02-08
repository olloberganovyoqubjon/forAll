package uz.forall.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.notes.eintity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByCategoryId(Long categoryId);
}
