package uz.forall.notes.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.forall.notes.eintity.Category;
import uz.forall.notes.eintity.Note;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryWithNotesDto {

    private Category category;
    private List<Note> notes;
}
