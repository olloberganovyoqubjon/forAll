package uz.forall.notes.service;

import org.springframework.stereotype.Service;
import uz.forall.notes.eintity.Note;
import uz.forall.notes.eintity.User;
import uz.forall.notes.payload.ApiResult;
import uz.forall.notes.payload.NoteDto;
import uz.forall.notes.repository.NoteRepository;
import uz.forall.notes.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    public NoteService(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }


    public ApiResult addNoteToCategory(NoteDto noteDto, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new ApiResult("User not found", false);
        }
        User user = optionalUser.get();


        Note note = new Note(null, noteDto.getAllDay(), noteDto.getTitle(), noteDto.getDescription(), noteDto.getColor(), noteDto.getStart(), noteDto.getEnd(), user);
        noteRepository.save(note);
        return new ApiResult("Note added", true);
    }

    public ApiResult editNoteToCategory(NoteDto noteDto, Long noteId, Long userId) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isEmpty()) {
            return new ApiResult("Note not found", false);
        }
        Note note = optionalNote.get();
        note.setEnd(noteDto.getEnd());
        note.setStart(noteDto.getStart());
        note.setTitle(noteDto.getTitle());
        note.setDescription(noteDto.getDescription());
        note.setColor(noteDto.getColor());
        note.setAllDay(noteDto.getAllDay());
        noteRepository.save(note);
        return new ApiResult("Note edited", true);
    }

    public ApiResult deleteNoteToCategory(Long noteId, Long userId) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isEmpty()) {
            return new ApiResult("Note not found", false);
        }
        Note note = optionalNote.get();
//        if (!note.getCategory().getUserId().equals(userId)) {
//            return new ApiResult("Note not found", false);
//        }
        noteRepository.delete(note);
        return new ApiResult("Note deleted", true);
    }

    public ApiResult getNote(Long noteId, Long userId) {
//        Optional<Note> optionalNote = noteRepository.findById(noteId);
//        if (optionalNote.isEmpty()) {
//            return new ApiResult("Note not found", false);
//        }
//        Note note = optionalNote.get();
//        if (!note.getCategory().getUserId().equals(userId)) {
//            return new ApiResult("Note not found", false);
//        }
        return new ApiResult("Note found", true, new Note());
    }

    public ApiResult getNotesToCategory(Long categoryId, Long userId) {
//        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
//        if (optionalCategory.isEmpty()) {
//            return new ApiResult("Category not found", false);
//        }
//        Category category = optionalCategory.get();
//        if (!category.getUserId().equals(userId)) {
//            return new ApiResult("Category not found", false);
//        }
        return new ApiResult("Notes found", true, null);
    }

    public ApiResult getNotesToUser(Long userId) {
        List<Note> noteList = noteRepository.findAllByUser_UserId(userId);
        return new ApiResult("Notes found", true, noteList);
    }

    public ApiResult changeCategory(Long noteId, Long categoryId, Long userId) {
//        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
//        if (optionalCategory.isEmpty()) {
//            return new ApiResult("Category not found", false);
//        }
//        Category category = optionalCategory.get();
//        if (!category.getUserId().equals(userId)) {
//            return new ApiResult("Category not found", false);
//        }
//        Optional<Note> optionalNote = noteRepository.findById(noteId);
//        if (optionalNote.isEmpty()) {
//            return new ApiResult("Note not found", false);
//        }
//        Note note = optionalNote.get();
//        note.setCategory(category);
//        noteRepository.save(note);
        return new ApiResult("Note changed category", true);
    }
}
