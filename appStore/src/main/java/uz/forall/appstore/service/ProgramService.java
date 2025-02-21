package uz.forall.appstore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.appstore.entity.Program;
import uz.forall.appstore.payload.ApiResult;
import uz.forall.appstore.payload.ProgramDto;
import uz.forall.appstore.repository.ProgramRepository;

import java.io.File;
import java.util.Objects;

@Service
public class ProgramService {

    String uploadDir = "files/appstore";

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public ApiResult addProgram(ProgramDto programDto, MultipartFile file) {
        try {
            Program programEntity = new Program(
                    null,
                    programDto.getName(),
                    programDto.getDescription(),
                    file.getOriginalFilename()
            );
            programEntity.setPath(file.getOriginalFilename());
            programRepository.save(programEntity);
            File target = new File(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(target);
            return new ApiResult("Program added successfully", true);
        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }
    }
}
