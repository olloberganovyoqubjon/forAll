package uz.forall.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.notes.eintity.Note;

import java.util.List;


public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByUser_UserId(Long userId);
}
