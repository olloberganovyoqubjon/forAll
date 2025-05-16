package uz.murojaat.appeal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.murojaat.appeal.model.Message;
import uz.murojaat.appeal.model.User;
import uz.murojaat.appeal.payload.ApiResult;
import uz.murojaat.appeal.repository.MessageRepository;
import uz.murojaat.appeal.repository.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;



    public ApiResult getMessages(Long senderUserId, Long receiverUserId) {
        Optional<User> optionalSenderUser = userRepository.findById(senderUserId);
        Optional<User> optionalReceiverUser = userRepository.findById(receiverUserId);
        if (optionalSenderUser.isEmpty() || optionalReceiverUser.isEmpty()) {
            return new ApiResult("Bunday foydalanuvchi mavjud emas!", false);
        }
        List<Message> messageList = messageRepository.findBySenderUser_IdAndReceiverUser_Id(senderUserId, receiverUserId);
        return new ApiResult("barcha habarlar", true, messageList);
    }

    public Boolean saveMessage(Message message) {
        try {
            messageRepository.save(message);
            return true;
        } catch (Exception e){
            return false;
        }
    }

}
