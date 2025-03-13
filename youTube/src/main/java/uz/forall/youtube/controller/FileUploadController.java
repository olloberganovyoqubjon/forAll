package uz.forall.youtube.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.youtube.payload.ApiResult;
import uz.forall.youtube.service.FileUploadService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("upload")
public class FileUploadController {

    private final FileUploadService fileUploadService;


    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/chunk")
    public HttpEntity<?> uploadChunk(@RequestParam("file") MultipartFile file,
                                     @RequestParam("chunkIndex") int chunkIndex,
                                     @RequestParam("totalChunks") int totalChunks,
                                     @RequestParam("fileName") String fileName,
                                     @RequestParam("categoryName") String categoryName,
                                     @RequestParam("playlistName") String playlistName,
                                     @RequestParam("webp") Boolean webp
    ) throws IOException {

        ApiResult apiResult = fileUploadService.saveVideo(file, fileName, chunkIndex, totalChunks, categoryName, playlistName, webp);
        return ResponseEntity.ok(apiResult);
    }


    @PostMapping("/webp")
    public HttpEntity<?> uploadWebp(
            @RequestParam("webpFile") MultipartFile webpFile,
            @RequestParam("fileName") String fileName) {

        ApiResult apiResult = fileUploadService.saveWebp(webpFile, fileName);
        return ResponseEntity.ok(apiResult);
    }


}