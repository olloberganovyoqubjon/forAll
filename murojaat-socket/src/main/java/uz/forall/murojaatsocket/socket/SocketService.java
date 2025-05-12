package uz.forall.murojaatsocket.socket;

import uz.forall.murojaatsocket.model.Message;
import uz.forall.murojaatsocket.model.User;
import uz.forall.murojaatsocket.payload.MessageDto;
import uz.forall.murojaatsocket.repository.UserRepository;
import uz.forall.murojaatsocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {


    private final MessageService messageService;
    private final UserRepository userRepository;

    public boolean saveMessage(MessageDto messageDto, boolean isRead) {

        Optional<User> optionalSenderUser = userRepository.findById(messageDto.getSenderUserId());
        Optional<User> optionalReceiverUser = userRepository.findById(messageDto.getReceiverUserId());
        if (optionalSenderUser.isEmpty() || optionalReceiverUser.isEmpty()) {
            return false;
        }

        return  messageService.saveMessage(Message.builder()
                .content(messageDto.getContent())
                .senderUser(optionalSenderUser.get())
                .receiverUser(optionalReceiverUser.get())
                .isRead(isRead)
                .build());
    }
}
