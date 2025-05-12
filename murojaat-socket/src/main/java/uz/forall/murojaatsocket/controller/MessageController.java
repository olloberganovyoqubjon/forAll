package uz.forall.murojaatsocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.murojaatsocket.payload.ApiResult;
import uz.forall.murojaatsocket.service.MessageService;

@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("{senderUserId}/{receiverUserId}")
    public HttpEntity<?> getMessages(@PathVariable Long senderUserId, @PathVariable Long receiverUserId) {
        ApiResult apiResult = messageService.getMessages(senderUserId,receiverUserId);
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }


}
