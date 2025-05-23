package uz.forall.appstore.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProgramResponse {
    private UUID id;

    private String name;            // Dastur nomi
    private String description;     // Dastur haqida qisqacha ma'lumot
    private String mainFile;        // Asosiy ishga tushiriladigan fayl nomi
    private byte[] icon;            // Dastur rasmi
    private String version;         // Dastur versiyasi
    private Boolean isDesktop;      // ishchi stolga chiqishi

    private Boolean isStartup;      // pusk menyusiga chiqishi

    private Boolean isAutoStart;    // windows yonganda avtomatik ishga tushishi
}
