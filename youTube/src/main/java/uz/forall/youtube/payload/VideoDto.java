package uz.forall.youtube.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {

    private Long id;

    private String title;

    private String bytes;

    private Boolean webp;
}
