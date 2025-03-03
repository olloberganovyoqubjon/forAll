package uz.forall.notes.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private Boolean allDay;
    private String title;
    private String description;
    private String color;
    private Date start;
    private Date end;
}
