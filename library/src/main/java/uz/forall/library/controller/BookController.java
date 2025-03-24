package uz.forall.library.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.library.payload.ApiResult;
import uz.forall.library.payload.BookDto;
import uz.forall.library.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Category asosida qidirish
    @GetMapping("category/{categoryId}")
    public HttpEntity<?> getBooksByCategory(@PathVariable UUID categoryId) {
        ApiResult apiResult = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);

    }

    // Fayl turi asosida qidirish
    @GetMapping("type/{fileType}")
    public HttpEntity<?> getBooksByFileType(@PathVariable String fileType) {
        ApiResult apiResult = bookService.getBooksByFileType(fileType);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    // Category va fayl turi asosida qidirish
    @GetMapping("category/{categoryId}/type/{fileType}")
    public HttpEntity<?> getBooksByCategoryAndFileType(@PathVariable UUID categoryId, @PathVariable String fileType) {
        ApiResult apiResult = bookService.getBooksByCategoryAndFileType(categoryId, fileType);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }


    // Fayl yuklash
    @PostMapping("upload/{categoryId}")
    public HttpEntity<?> uploadFile(@PathVariable UUID categoryId, @RequestParam("file") MultipartFile file) {
        ApiResult apiResult = bookService.saveFile(categoryId, file);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    // Faylni olish (PDF, MP4, MP3)
    @GetMapping("{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) {
        ApiResult apiResult = bookService.getFile(id);

        // Agar fayl topilmasa 404 qaytaramiz
        if (!apiResult.isSuccess()) {
            return ResponseEntity.notFound().build();
        }

        // ApiResult ichidan BookDto ni olish
        BookDto book = (BookDto) apiResult.getObject();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(book.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + book.getName() + "\"")
                .body(book.getData());
    }
}
