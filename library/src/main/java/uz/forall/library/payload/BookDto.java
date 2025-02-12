package uz.forall.library.payload;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private String fileType;  // pdf, mp3, video
    private String name;
    @Lob
    private byte[] data;
}
