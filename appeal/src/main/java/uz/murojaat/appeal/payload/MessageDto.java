package uz.murojaat.appeal.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private String content;

    private Long senderUserId;

    private Long receiverUserId;
}
