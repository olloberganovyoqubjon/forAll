package uz.murojaat.appeal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
//import uz.forall.murojaatsocket.model.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Message extends BaseModel {

    private String content;

    @ManyToOne
    private User senderUser;

    @ManyToOne
    private User receiverUser;

    private Boolean isRead;
}
