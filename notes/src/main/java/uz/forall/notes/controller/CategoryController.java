package uz.forall.notes.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.notes.payload.ApiResult;
import uz.forall.notes.payload.CategoryDto;
import uz.forall.notes.service.CategoryService;

@RestController
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("add/{userId}")
    public HttpEntity<?> addCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long userId) {
        ApiResult apiResult = categoryService.addCategory(categoryDto, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @DeleteMapping("delete/{categoryId}/{userId}")
    public HttpEntity<?> deleteCategory(@PathVariable Long categoryId, @PathVariable Long userId) {
        ApiResult apiResult = categoryService.deleteCategory(categoryId, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @PutMapping("edit/{categoryId}/{userId}")
    public HttpEntity<?> editCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto, @PathVariable Long userId) {
        ApiResult apiResult = categoryService.editCategory(categoryId, categoryDto, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("all/{userId}")
    public HttpEntity<?> getAllCategories(@PathVariable Long userId) {
        ApiResult apiResult = categoryService.getAllCategories(userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("get/{categoryId}/{userId}")
    public HttpEntity<?> getCategory(@PathVariable Long categoryId, @PathVariable Long userId) {
        ApiResult apiResult = categoryService.getCategory(categoryId, userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
