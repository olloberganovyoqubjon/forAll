package uz.murojaat.appeal.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.murojaat.appeal.model.Message;
import uz.murojaat.appeal.model.User;
import uz.murojaat.appeal.payload.MessageDto;
import uz.murojaat.appeal.repository.UserRepository;
import uz.murojaat.appeal.service.MessageService;

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
