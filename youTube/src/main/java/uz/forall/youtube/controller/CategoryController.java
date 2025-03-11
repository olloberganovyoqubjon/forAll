package uz.forall.youtube.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.service.CategoryService;

@RestController
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("add")
    public HttpEntity<?> addCategory(@RequestBody Category category) {
        ApiResult apiResult = categoryService.addCategory(category);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("categories")
    public HttpEntity<?> getCategories() {
        ApiResult apiResult = categoryService.getCategories();
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("category/{id}")
    public HttpEntity<?> getCategory(@PathVariable Long id) {
        ApiResult apiResult = categoryService.getCategory(id);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @PutMapping("update")
    public HttpEntity<?> updateCategory(@RequestBody Category category) {
        ApiResult apiResult = categoryService.updateCategory(category);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @DeleteMapping("delete/{id}")
    public HttpEntity<?> deleteCategory(@PathVariable Long id) {
        ApiResult apiResult = categoryService.deleteCategory(id);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
