package uz.forall.appstore.payload;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDto {

    private String name; // Dastur nomi
    private String description; // Dastur haqida qisqacha ma'lumot
    private String mainFile; // Asosiy ishga tushiriladigan fayl nomi
    private String version; // Dastur versiyasi

    @Lob
    private byte[] icon; // Dastur rasmi

    @ElementCollection
    private List<Map<String, String>> files; // Papkada saqlangan barcha fayllar
}
