package uz.forall.murojaatsocket.controller;

//import com.example.demo.model.Message;
//import com.example.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.murojaatsocket.model.Message;
import uz.forall.murojaatsocket.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }


}
