package uz.forall.appstore.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.forall.appstore.entity.Software;
import uz.forall.appstore.entity.User;
import uz.forall.appstore.payload.AllProgramResponse;
import uz.forall.appstore.payload.ApiResult;
import uz.forall.appstore.payload.ProgramDto;
import uz.forall.appstore.repository.ProgramRepository;
import uz.forall.appstore.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static uz.forall.appstore.AppStoreApplication.uploadDir;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;

    public ProgramService(ProgramRepository programRepository, UserRepository userRepository) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<?> getProgram(UUID softwareId, Long userId) throws IOException {
        Optional<Software> optionalSoftware = programRepository.findById(softwareId);
        if (optionalSoftware.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dastur topilmadi");
        }

        // Papkada saqlangan barcha fayllar ro‘yxatini olish
        File[] files = new File(uploadDir + "/" + softwareId).getAbsoluteFile().listFiles();
        if (files == null || files.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Papkada fayl mavjud emas");
        }

        ProgramDto programDto = new ProgramDto();
        programDto.setName(optionalSoftware.get().getName());
        programDto.setDescription(optionalSoftware.get().getDescription());
        programDto.setMainFile(optionalSoftware.get().getMainFile());
        programDto.setVersion(optionalSoftware.get().getVersion());
        programDto.setIcon(optionalSoftware.get().getIcon());

        List<Map<String, String>> fileList = new ArrayList<>();

        for (File file : files) {
            Path path = file.toPath();
            byte[] fileBytes = Files.readAllBytes(path);
            String base64 = Base64.getEncoder().encodeToString(fileBytes);

            Map<String, String> fileData = new HashMap<>();
            fileData.put("fileName", file.getName());
            fileData.put("fileData", base64);
            fileList.add(fileData);
        }
        programDto.setFiles(fileList);

        Optional<User> optionalUser = userRepository.findUserByUserIdAndSoftware_Id(userId, softwareId);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setVersion(optionalUser.get().getVersion());
        } else {
            user.setUserId(userId);
            user.setVersion(optionalSoftware.get().getVersion());
            user.setSoftware(optionalSoftware.get());
        }
        userRepository.save(user);

        return ResponseEntity.ok(programDto);
    }

    public ApiResult saveSoftware(String name, String description, String mainFile, String version, MultipartFile icon, MultipartFile[] files) throws IOException {

        // Dasturni saqlash
        Software software = Software.builder()
                .name(name)
                .description(description)
                .mainFile(mainFile)
                .version(version)
                .icon(icon.getBytes())
                .build();
        Software savedSoftware = programRepository.save(software);

        // Fayllarni saqlash uchun katalog yaratish
        String dirPath = uploadDir + "/" + savedSoftware.getId();
        File file = new File(dirPath);
        file.mkdir();

        // Fayllarni saqlash
        for (MultipartFile multipartFile : files) {
            String filePath = dirPath + "/" + multipartFile.getOriginalFilename(); // Har bir faylni o‘z nomi bilan saqlash
            Files.copy(multipartFile.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
        }
        return new ApiResult("Dastur muvaffaqiyatli saqlandi!", true);
    }

    public ApiResult getAllSoftware(Long userId) {
        List<Software> programRepositoryAll = programRepository.findAll();
        List<AllProgramResponse> allProgramResponses = new ArrayList<>();
        for (Software software : programRepositoryAll) {
            Optional<User> optionalUser = userRepository.findUserByUserIdAndSoftware_Id(userId, software.getId());
            String userVersion = null;
            if (optionalUser.isPresent()) {
                userVersion = optionalUser.get().getVersion();
            }
            allProgramResponses.add(
                    new AllProgramResponse(
                            software.getId(), software.getName(), software.getVersion(), software.getMainFile(), software.getIcon(), software.getVersion(), userVersion));
        }

        return new ApiResult("Barcha dasturlar", true, allProgramResponses);
    }
}
