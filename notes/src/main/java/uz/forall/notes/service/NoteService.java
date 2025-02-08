package uz.forall.notes.service;

import org.springframework.stereotype.Service;
import uz.forall.notes.eintity.Category;
import uz.forall.notes.eintity.Note;
import uz.forall.notes.payload.ApiResult;
import uz.forall.notes.payload.CategoryWithNotesDto;
import uz.forall.notes.payload.NoteDto;
import uz.forall.notes.repository.CategoryRepository;
import uz.forall.notes.repository.NoteRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final CategoryRepository categoryRepository;
    private final NoteRepository noteRepository;

    public NoteService(CategoryRepository categoryRepository, NoteRepository noteRepository) {
        this.categoryRepository = categoryRepository;
        this.noteRepository = noteRepository;
    }


    public ApiResult addNoteToCategory(NoteDto noteDto, Long categoryId, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            return new ApiResult("Category not found", false);
        }
        Category category = optionalCategory.get();
        if (!category.getUserId().equals(userId)) {
            return new ApiResult("Category not found", false);
        }
        Note note = new Note(null, category, noteDto.getTitle(), noteDto.getContent(), new Timestamp(System.currentTimeMillis()), noteDto.getTermDate());
        noteRepository.save(note);
        return new ApiResult("Note added", true);
    }

    public ApiResult editNoteToCategory(NoteDto noteDto, Long noteId, Long userId) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isEmpty()) {
            return new ApiResult("Note not found", false);
        }
        Note note = optionalNote.get();
        if (!note.getCategory().getUserId().equals(userId)) {
            return new ApiResult("Note not found", false);
        }
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setTermDate(noteDto.getTermDate());
        noteRepository.save(note);
        return new ApiResult("Note edited", true);
    }

    public ApiResult deleteNoteToCategory(Long noteId, Long userId) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isEmpty()) {
            return new ApiResult("Note not found", false);
        }
        Note note = optionalNote.get();
        if (!note.getCategory().getUserId().equals(userId)) {
            return new ApiResult("Note not found", false);
        }
        noteRepository.delete(note);
        return new ApiResult("Note deleted", true);
    }

    public ApiResult getNote(Long noteId, Long userId) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isEmpty()) {
            return new ApiResult("Note not found", false);
        }
        Note note = optionalNote.get();
        if (!note.getCategory().getUserId().equals(userId)) {
            return new ApiResult("Note not found", false);
        }
        return new ApiResult("Note found", true, note);
    }

    public ApiResult getNotesToCategory(Long categoryId, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            return new ApiResult("Category not found", false);
        }
        Category category = optionalCategory.get();
        if (!category.getUserId().equals(userId)) {
            return new ApiResult("Category not found", false);
        }
        return new ApiResult("Notes found", true, noteRepository.findAllByCategoryId(categoryId));
    }

    public ApiResult getNotesToUser(Long userId) {
        List<Category> categoryList = categoryRepository.findAllByUserId(userId);
        if (categoryList.isEmpty()) {
            return new ApiResult("Category not found", false);
        }
        List<CategoryWithNotesDto> categoryWithNotesDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            List<Note> noteList = noteRepository.findAllByCategoryId(category.getId());
            categoryWithNotesDtoList.add(new CategoryWithNotesDto(category, noteList));
        }
        return new ApiResult("Notes found", true, categoryWithNotesDtoList);
    }

    public ApiResult changeCategory(Long noteId, Long categoryId, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            return new ApiResult("Category not found", false);
        }
        Category category = optionalCategory.get();
        if (!category.getUserId().equals(userId)) {
            return new ApiResult("Category not found", false);
        }
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isEmpty()) {
            return new ApiResult("Note not found", false);
        }
        Note note = optionalNote.get();
        note.setCategory(category);
        noteRepository.save(note);
        return new ApiResult("Note changed category", true);
    }
}
