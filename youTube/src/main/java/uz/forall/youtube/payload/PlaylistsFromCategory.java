package uz.forall.youtube.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistsFromCategory {

    private Long playlistId;
    private String categoryName;
    private Boolean active;
}
