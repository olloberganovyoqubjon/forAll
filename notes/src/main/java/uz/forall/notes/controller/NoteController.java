package uz.forall.notes.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.notes.payload.ApiResult;
import uz.forall.notes.payload.NoteDto;
import uz.forall.notes.service.NoteService;

@RestController
@RequestMapping
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping("add")
    public HttpEntity<?> addNote(@RequestBody NoteDto noteDto) {
        ApiResult apiResult = noteService.addNoteToCategory(noteDto, 1L);
        return ResponseEntity.status(apiResult.isSuccess() ? 201 : 409).body(apiResult);
    }

    @PutMapping("edit/{noteId}")
    public HttpEntity<?> editNote(@RequestBody NoteDto noteDto, @PathVariable Long noteId) {
        ApiResult apiResult = noteService.editNoteToCategory(noteDto, noteId, 1L);
        return ResponseEntity.status(apiResult.isSuccess() ? 202 : 409).body(apiResult);
    }

    @DeleteMapping("delete/{noteId}")
    public HttpEntity<?> deleteNote(@PathVariable Long noteId) {
        ApiResult apiResult = noteService.deleteNoteToCategory(noteId, 1L);
        return ResponseEntity.status(apiResult.isSuccess() ? 202 : 409).body(apiResult);
    }

    @GetMapping("note/{noteId}/{userId}")
    public HttpEntity<?> getNotes(@PathVariable Long noteId, @PathVariable Long userId) {
        ApiResult apiResult = noteService.getNote(noteId, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("notesToCategory/{categoryId}/{userId}")
    public HttpEntity<?> getNotesToCategory(@PathVariable Long categoryId, @PathVariable Long userId) {
        ApiResult apiResult = noteService.getNotesToCategory(categoryId, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("allNotes")
    public HttpEntity<?> getAllNotes() {
        ApiResult apiResult = noteService.getNotesToUser(1L);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("changeCategory/{noteId}/{categoryId}/{userId}")
    public HttpEntity<?> changeCategory(@PathVariable Long noteId, @PathVariable Long categoryId, @PathVariable Long userId) {
        ApiResult apiResult = noteService.changeCategory(noteId, categoryId, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 202 : 409).body(apiResult);
    }
}
