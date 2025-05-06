package uz.forall.murojaatsocket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Message extends BaseModel {

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String content;
    private String room;

    private String username;


}
