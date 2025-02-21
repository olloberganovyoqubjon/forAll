package uz.forall.appstore.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.appstore.entity.Program;
import uz.forall.appstore.payload.ApiResult;
import uz.forall.appstore.payload.ProgramDto;
import uz.forall.appstore.service.ProgramService;

@RestController
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping("/add")
    public HttpEntity<?> addProgram(@RequestBody ProgramDto programDto, @RequestPart("file") MultipartFile file) {
        ApiResult apiResult = programService.addProgram(programDto, file);
        return ResponseEntity.status(apiResult.isSuccess() ? 201 : 409).body(apiResult);
    }
}
