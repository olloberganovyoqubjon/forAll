package uz.forall.murojaatsocket.service;

import uz.forall.murojaatsocket.model.Message;
import uz.forall.murojaatsocket.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    public List<Message> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }

    public Message saveMessage(Message message) {
        System.out.println(message);
        return messageRepository.save(message);
    }

}
