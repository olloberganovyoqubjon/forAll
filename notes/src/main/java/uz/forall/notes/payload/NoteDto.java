package uz.forall.notes.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private String title;
    private String content;
    private Timestamp termDate;
}
