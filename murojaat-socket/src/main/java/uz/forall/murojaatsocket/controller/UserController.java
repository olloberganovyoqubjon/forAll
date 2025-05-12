package uz.forall.murojaatsocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.forall.murojaatsocket.payload.ApiResult;
import uz.forall.murojaatsocket.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("{organizationId}")
    public HttpEntity<ApiResult> getUsers(@PathVariable Long organizationId) {
        ApiResult apiResult = userService.getUsers(organizationId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("{userId}")
    public HttpEntity<ApiResult> getUser(@PathVariable Long userId) {
        ApiResult apiResult = userService.getUser(userId);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

}
