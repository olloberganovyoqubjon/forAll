package uz.forall.appstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "softwares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Software {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    private String name; // Dastur nomi
    private String description; // Dastur haqida qisqacha ma'lumot
    private String mainFile; // Asosiy ishga tushiriladigan fayl nomi

    @Lob
    private byte[] icon; // Dastur rasmi

    private String version; // Dastur versiyasi
}

