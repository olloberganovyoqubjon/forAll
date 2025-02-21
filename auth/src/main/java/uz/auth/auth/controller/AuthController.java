package uz.auth.auth.controller;

import uz.auth.auth.payload.ApiResult;
import uz.auth.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Основное назначение:
     *
     * @RestController: Объявление класса как контроллера REST API.
     * @RequestMapping("/api/auth"): Базовый путь для всех запросов, обрабатываемых этим контроллером.
     * @Tag(name = "AuthController", description = "Autentification uchun"): Аннотация Swagger для группировки операций в документации.
     * @Autowired AuthService service: Внедрение зависимости сервиса AuthService.
     * @PostMapping("/login"): Обработчик POST запроса для выполнения входа в систему.
     * @RequestBody LoginDto dto: Параметр запроса, содержащий данные для входа (имя пользователя и пароль).
     * ApiResult apiResult = service.login(dto);: Вызов метода login сервиса AuthService для выполнения аутентификации.
     * return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);:
     * Возврат HTTP ответа с кодом 201 Created в случае успешного входа и 409 Conflict в противном случае.
     * @GetMapping("/refreshToken"): Обработчик GET запроса для обновления токена доступа.
     * @RequestParam String token: Параметр запроса, содержащий токен, который нужно обновить.
     * ApiResult apiResult = service.updateAccessToken(token);: Вызов метода updateAccessToken сервиса AuthService
     * для обновления токена доступа.
     * return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);:
     * Возврат HTTP ответа с кодом 200 OK в случае успешного обновления и 409 Conflict в противном случае.
     * Примечания:
     * Контроллер предоставляет два основных HTTP метода для аутентификации и обновления токена.
     * Используется объект ApiResult для упрощения возврата результатов операций.
     * Используются статусы HTTP (например, HttpStatus.CREATED и HttpStatus.OK) для передачи информации о статусе операции.
     * В случае возникновения ошибки, обрабатываемой сервисом, возвращается HTTP статус 409 Conflict.
     */

    private final AuthService service;

//    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
//        this.authenticationService = authenticationService;
    }




    @GetMapping("/refreshToken")
    public HttpEntity<?> refreshToken(@RequestParam String token) {
        // Вызов сервисного метода для обновления токена доступа
        ApiResult apiResult = service.updateAccessToken(token);
        // Возврат HTTP ответа в зависимости от успешности операции
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }


//    @Operation(summary = "Авторизация пользователя")
//    @PostMapping("/sign-in")
//    public HttpEntity<?> signIn(@RequestBody @Valid SignInRequest request) {
//        AuthenticationResponse authenticate = authenticationService.signIn(request);
//        if (authenticate.getAccessToken() == null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authenticate);
//        return ResponseEntity.status(HttpStatus.OK).body(authenticate);
//
//    }

    @GetMapping("validate")
    public HttpEntity<?> validateToken(@RequestParam String token) {
        ApiResult apiResult = service.validateToken(token);
        if (apiResult.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}



