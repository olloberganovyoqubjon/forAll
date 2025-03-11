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
@RequestMapping
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
            @RequestParam MultipartFile file,
            @RequestParam Boolean isDesktop,
            @RequestParam Boolean isStartup,
            @RequestParam Boolean isAutoStart
            ) throws IOException {

        ApiResult apiResult = programService.saveSoftware(name, description, mainFile, version, icon, file, isDesktop, isStartup, isAutoStart);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/download/{softwareId}")
    public HttpEntity<?> downloadSoftware(@PathVariable UUID softwareId) throws IOException {
        return programService.getProgram(softwareId);
    }

    @GetMapping("/getAllSoftware")
    public HttpEntity<?> getAllSoftware(){
        ApiResult apiResult = programService.getAllSoftware();
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @DeleteMapping("/delete/{softwareId}")
    public HttpEntity<?> deleteSoftware(@PathVariable UUID softwareId){
        ApiResult apiResult = programService.deleteSoftware(softwareId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
