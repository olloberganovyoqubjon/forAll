package uz.forall.youtube.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.service.AnalyzeService;

@RestController
@RequestMapping("analyze")
public class AnalyzeController {


    private final AnalyzeService analyzeService;

    public AnalyzeController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @GetMapping("analyzeVideoWithImage")
    public HttpEntity<?> analyzeVideoWithImage(@RequestParam String categoryName) {
        ApiResult apiResult = analyzeService.analyzeVideoWithImage(categoryName, null, null);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("analyzeVideo")
    public HttpEntity<?> analyzeVideo(@RequestParam String folderPath, @RequestParam String categoryName) {
        ApiResult apiResult = analyzeService.analyzeVideo(folderPath, categoryName, null);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
