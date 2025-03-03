package uz.forall.notes.eintity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean allDay;
    private String title;
    private String description;
    private String color;
    @Column(name = "start_date")
    private Date start;
    @Column(name = "end_date")
    private Date end;

    @ManyToOne
    private User user;
}
