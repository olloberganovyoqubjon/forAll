package uz.forall.appstore.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.appstore.annotation.CurrentUserId;
import uz.forall.appstore.payload.ApiResult;
import uz.forall.appstore.service.ProgramService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("program")
@CrossOrigin(origins = "*")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping("/upload")
    public HttpEntity<?> uploadSoftware(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String mainFile,
            @RequestParam String version,
            @RequestParam MultipartFile icon,
            @RequestParam MultipartFile[] files) throws IOException {

        ApiResult apiResult = programService.saveSoftware(name, description, mainFile, version, icon, files);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/download/{softwareId}")
    public HttpEntity<?> downloadSoftware(@PathVariable UUID softwareId, @CurrentUserId Long currentUserId) throws IOException {
        return programService.getProgram(softwareId, currentUserId);
    }

    @GetMapping("/getAllSoftwares")
    public HttpEntity<?> getAllSoftwares(@CurrentUserId Long userId){
        ApiResult apiResult = programService.getAllSoftware(userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
