package uz.forall.telephone.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tables")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Table {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String shortNum;

    private String longNum;

    private String dateBirth;

    @ManyToOne
    private Category category;
}
