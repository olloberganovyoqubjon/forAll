
package uz.auth.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "divisions")
@Data
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Определение свойств колонки в базе данных.
    private String name; // Название подразделения.

    @Column(nullable = true) // Определение свойств колонки в базе данных.
    private String shortName; // Краткое название подразделения.

    @Column(nullable = true) // Определение свойств колонки в базе данных.
    private boolean delete = false; // Флаг удаления подразделения (по умолчанию не удалено).

    @Column(nullable = true) // Определение свойств колонки в базе данных.
    private String index; // Индекс подразделения.

}




