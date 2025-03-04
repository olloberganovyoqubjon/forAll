package uz.forall.appstore.payload;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProgramResponse {
    private UUID id;

    private String name; // Dastur nomi
    private String description; // Dastur haqida qisqacha ma'lumot
    private String mainFile; // Asosiy ishga tushiriladigan fayl nomi
    private byte[] icon; // Dastur rasmi
    private String version; // Dastur versiyasi
}
