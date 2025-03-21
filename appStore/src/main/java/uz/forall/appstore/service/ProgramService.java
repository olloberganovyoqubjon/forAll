package uz.forall.appstore.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.appstore.entity.Software;
import uz.forall.appstore.payload.AllProgramResponse;
import uz.forall.appstore.payload.ApiResult;
import uz.forall.appstore.payload.ProgramDto;
import uz.forall.appstore.repository.ProgramRepository;

import java.io.IOException;
import java.util.*;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }


    public ResponseEntity<?> getProgram(UUID softwareId) throws IOException {
        Optional<Software> optionalSoftware = programRepository.findById(softwareId);
        if (optionalSoftware.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dastur topilmadi");
        }

        ProgramDto programDto = new ProgramDto();
        programDto.setId(optionalSoftware.get().getId());
        programDto.setName(optionalSoftware.get().getName());
        programDto.setDescription(optionalSoftware.get().getDescription());
        programDto.setMainFile(optionalSoftware.get().getMainFile());
        programDto.setVersion(optionalSoftware.get().getVersion());
        programDto.setIcon(optionalSoftware.get().getIcon());
        programDto.setFile(optionalSoftware.get().getFile());
//
//        Path path = new File(uploadDir + "/" + softwareId + ".rar").toPath();
//        byte[] fileBytes = Files.readAllBytes(path);
//        String base64 = Base64.getEncoder().encodeToString(fileBytes);
//
//        Map<String, String> fileData = new HashMap<>();
//        fileData.put("fileName", path.getFileName().toString());
//        fileData.put("fileData", base64);
//        programDto.setFile(fileData);
        return ResponseEntity.ok(programDto);
    }

    public ApiResult saveSoftware(String name, String description, String mainFile, String version, MultipartFile multipartIcon, MultipartFile multipartFile, Boolean isDesktop, Boolean isStartup, Boolean isAutoStart) throws IOException {

        // Dasturni saqlash
        Software software = Software.builder()
                .name(name)
                .description(description)
                .mainFile(mainFile)
                .version(version)
                .icon(multipartIcon.getBytes())
                .file(multipartFile.getBytes())
                .isDesktop(isDesktop)
                .isStartup(isStartup)
                .isAutoStart(isAutoStart)
                .build();
        programRepository.save(software);
        return new ApiResult("Dastur muvaffaqiyatli saqlandi!", true);
    }

    public ApiResult getAllSoftware() {
        List<Software> programRepositoryAll = programRepository.findAll();
        List<AllProgramResponse> allProgramResponses = new ArrayList<>();
        for (Software software : programRepositoryAll) {
            allProgramResponses.add(
                    new AllProgramResponse(
                            software.getId(), software.getName(), software.getDescription(), software.getMainFile(), software.getIcon(), software.getVersion(), software.getIsDesktop(),software.getIsStartup(), software.getIsAutoStart()));
        }

        return new ApiResult("Barcha dasturlar", true, allProgramResponses);
    }

    public ApiResult deleteSoftware(UUID softwareId) {
        programRepository.deleteById(softwareId);
        return new ApiResult("Dastur muvaffaqiyatli o'chirildi!", true);
    }

    public ApiResult updateSoftware(UUID id, String name, String description, String mainFile, String version, MultipartFile icon, MultipartFile file, Boolean isDesktop, Boolean isStartup, Boolean isAutoStart) throws IOException {

        // Dasturni saqlash
        Software software = Software.builder()
                .id(id)
                .name(name)
                .description(description)
                .mainFile(mainFile)
                .version(version)
                .icon(icon.getBytes())
                .file(file.getBytes())
                .isDesktop(isDesktop)
                .isStartup(isStartup)
                .isAutoStart(isAutoStart)
                .build();
        programRepository.save(software);
        return new ApiResult("Dastur muvaffaqiyatli o'zgartirildi!", true);
    }
}
