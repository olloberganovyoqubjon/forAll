package uz.murojaat.appeal.model;

import jakarta.persistence.Entity;
import lombok.*;
//import uz.forall.murojaatsocket.model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Organization extends BaseModel {
    private String name;
}
