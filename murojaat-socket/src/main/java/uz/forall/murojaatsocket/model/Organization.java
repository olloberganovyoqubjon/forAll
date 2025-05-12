package uz.forall.murojaatsocket.model;

import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Organization extends BaseModel{
    private String name;
}
