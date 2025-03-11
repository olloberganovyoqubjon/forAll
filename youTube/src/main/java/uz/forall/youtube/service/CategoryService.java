package uz.forall.youtube.service;

import org.springframework.stereotype.Service;
import uz.forall.youtube.entity.Category;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.repository.CategoryRepository;
import uz.forall.youtube.repository.VideoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final VideoRepository videoRepository;
    private final VideoService videoService;

    public CategoryService(CategoryRepository categoryRepository, VideoRepository videoRepository, VideoService videoService) {
        this.categoryRepository = categoryRepository;
        this.videoRepository = videoRepository;
        this.videoService = videoService;
    }


    public ApiResult addCategory(Category category) {
        categoryRepository.save(category);
        return new ApiResult("Kategoriya qo'shildi", true);
    }

    public ApiResult getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return new ApiResult("Kategoriyalar ro'yxati", true, categoryList);
    }

    public ApiResult getCategory(Long id) {
        ApiResult videos = videoService.getVideos(id, 0, 20);
//        Optional<Category> optionalCategory = categoryRepository.findById(id);
//        return optionalCategory.map(category -> new ApiResult("Kategoriya topildi", true, category)).orElseGet(() -> new ApiResult("Kategoriya topildi", true));
        return new ApiResult("Kategoriya topildi", true, videos);
    }

    public ApiResult updateCategory(Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
        if (optionalCategory.isEmpty()) return new ApiResult("Kategoriya topilmadi", false);
        categoryRepository.save(category);
        return new ApiResult("Kategoriya o'zgartirildi", true);
    }

    public ApiResult deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) return new ApiResult("Kategoriya topilmadi", false);
        categoryRepository.deleteById(id);
        return new ApiResult("Kategoriya o'chirildi", true);
    }
}
