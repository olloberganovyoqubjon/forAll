package uz.forall.notes.service;

import org.springframework.stereotype.Service;
import uz.forall.notes.eintity.Category;
import uz.forall.notes.payload.ApiResult;
import uz.forall.notes.payload.CategoryDto;
import uz.forall.notes.repository.CategoryRepository;

import java.util.Optional;

import static org.springframework.data.repository.util.ClassUtils.ifPresent;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ApiResult addCategory(CategoryDto categoryDto, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findByNameAndUserId(categoryDto.getName(), userId);
        if (optionalCategory.isPresent()) {
            return new ApiResult("Category already exists", false);
        }
        Category category = new Category(null, userId, categoryDto.getName());
        categoryRepository.save(category);
        return new ApiResult("Category added", true);
    }

    public ApiResult deleteCategory(Long categoryId, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (optionalCategory.isEmpty()) {
            return new ApiResult("Category not found", false);
        }
        Category category = optionalCategory.get();
        if (!category.getUserId().equals(userId)) {
            return new ApiResult("Category not found", false);
        }
        categoryRepository.deleteById(categoryId);
        return new ApiResult("Category deleted", true);
    }

    public ApiResult editCategory(Long categoryId, CategoryDto categoryDto, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (optionalCategory.isEmpty()) {
            return new ApiResult("Category not found", false);
        }
        Category category = optionalCategory.get();
        if (!category.getUserId().equals(userId)) {
            return new ApiResult("Category not found", false);
        }
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return new ApiResult("Category edited", true);
    }

    public ApiResult getAllCategories(Long userId) {
        return new ApiResult("All categories", true, categoryRepository.findAllByUserId(userId));
    }

    public ApiResult getCategory(Long categoryId, Long userId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndUserId(categoryId, userId);
        return optionalCategory.map(category -> new ApiResult("One category", true, category)).orElseGet(() -> new ApiResult("Category not found", false));
    }
}
