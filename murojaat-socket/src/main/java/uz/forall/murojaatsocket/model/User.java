package uz.forall.murojaatsocket.model;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@Table(name = "users")
public class User extends BaseModel{

    private String username;

    @ManyToOne
    private Organization organization;

    private Boolean isOrderly;
}
