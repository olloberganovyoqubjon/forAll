package uz.murojaat.appeal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.murojaat.appeal.payload.ApiResult;
import uz.murojaat.appeal.service.MessageService;

@RestController
@RequestMapping("message")
//@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("{senderUserId}/{receiverUserId}")
    public HttpEntity<?> getMessages(@PathVariable Long senderUserId, @PathVariable Long receiverUserId) {
        ApiResult apiResult = messageService.getMessages(senderUserId,receiverUserId);
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }


}
