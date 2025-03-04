package uz.forall.appstore.payload;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDto {

    private UUID id;
    private String name; // Dastur nomi
    private String description; // Dastur haqida qisqacha ma'lumot
    private String mainFile; // Asosiy ishga tushiriladigan fayl nomi
    private String version; // Dastur versiyasi

    @Lob
    private byte[] icon; // Dastur rasmi

    @Lob
    private byte[] file; // dastur fayli
}
