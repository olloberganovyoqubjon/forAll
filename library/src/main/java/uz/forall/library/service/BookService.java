package uz.forall.library.service;

import org.springframework.web.multipart.MultipartFile;
import uz.forall.library.entity.Book;
import uz.forall.library.entity.Category;
import uz.forall.library.payload.ApiResult;
import uz.forall.library.payload.BookDto;
import uz.forall.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import uz.forall.library.repository.CategoryRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.forall.library.LibraryApplication.uploadDir;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public ApiResult getBooksByCategory(UUID categoryId) {
        List<Book> byCategoryId = bookRepository.findByCategoryId(categoryId);
        return new ApiResult("Books by category", true, byCategoryId);
    }

    public ApiResult getBooksByFileType(String fileType) {
        List<Book> byFileType = bookRepository.findByFileType(fileType);
        return new ApiResult("Books by file type", true, byFileType);
    }

    public ApiResult getBooksByCategoryAndFileType(UUID categoryId, String fileType) {
        List<Book> byCategoryIdAndFileType = bookRepository.findByCategoryIdAndFileType(categoryId, fileType);
        return new ApiResult("Books by category and file type", true, byCategoryIdAndFileType);
    }


    public ApiResult saveFile(UUID categoryId, MultipartFile file) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            return new ApiResult("Category not found", false);
        }

        Category category = categoryOptional.get();

        // Book obyektini yaratish va saqlash
        Book book = new Book(null, file.getContentType(), file.getOriginalFilename(), category);
        Book savedBook = bookRepository.save(book); // Bu yerda bookId (UUID) avtomatik yaratiladi

        // Faylni bookId bilan saqlash
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String filePath = uploadDir + savedBook.getId() + (fileExtension.isEmpty() ? "" : "." + fileExtension);

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            return new ApiResult("Error saving file: " + e.getMessage(), false);
        }

        return new ApiResult("File saved successfully with bookId: " + savedBook.getId(), true);
    }

    // Fayl kengaytmasini olish uchun yordamchi metod
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }


    public ApiResult getFile(UUID id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return new ApiResult("File not found", false);
        }
        Book book = optionalBook.get();
        // Faylni olish joyi (masalan, uploads papkasi)


        // Fayl kengaytmasini olish (agar kerak bo'lsa)
        String fileExtension = getFileExtension(book.getName());
        String filePath = uploadDir + book.getId() + (fileExtension.isEmpty() ? "" : "." + fileExtension);

        File file = new File(filePath);
        if (!file.exists()) {
            return new ApiResult("File not found on server", false);
        }

        try {
            // Faylni byte[] formatida o'qish
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fileInputStream.read(fileData);
            fileInputStream.close();

            // BookDto ni to'ldirish
            BookDto bookDto = new BookDto();
            bookDto.setFileType(book.getFileType());
            bookDto.setName(book.getName());
            bookDto.setData(fileData);

            return new ApiResult("File found", true, bookDto);

        } catch (IOException e) {
            return new ApiResult("Error reading file: " + e.getMessage(), false);
        }
    }
}
