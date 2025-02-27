package uz.auth.auth.controller;

import uz.auth.auth.annotation.CurrentUser;
import uz.auth.auth.annotation.RoleniTekshirish;
import uz.auth.auth.entity.Role;
import uz.auth.auth.entity.Users;
import uz.auth.auth.payload.ApiResult;
import uz.auth.auth.payload.EditUserDto;
import uz.auth.auth.payload.LoginDto;
import uz.auth.auth.payload.RegisterDto;
import uz.auth.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }





    /**
     * Sistemaga kirish uchun
     * @param dto username va password keladi
     * @return ApiResult natijani beradi true or false
     */
    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto dto) {
        // Вызов сервисного метода для выполнения входа в систему
        ApiResult apiResult = service.login(dto);
        // Возврат HTTP ответа в зависимости от успешности операции
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }


    /**
     * Userni ro'yxatdan o'tkaziish uchun kerak
     *
     * @param dto orqali quyidagilar keladi
     *            <p>
     *            private String fullName;
     *            <p>
     *            private String username;
     *            <p>
     *            private String password;
     *            <p>
     *            private String prePassword;
     *            <p>
     *            private Long divisionId;
     *            <p>
     *            private Long roleId;
     * @return ApiResult orqali natija qaytadi
     * @PreAuthorize orqali user permissioni (huquqi) tekshiriladi
     * @Valid orqali json qiymatlar to'g'ri kelganligi tekshiriladi
     */


//    @PreAuthorize(value = "hasAuthority('ADD_USER')") permission (huquq bo'yicha tekshirish)
    @RoleniTekshirish(role = "ADMIN")
    @PostMapping("/addUser")
    public HttpEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        ApiResult apiResult = service.register(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }

    /**
     * Userni parametrlarini o'zgartirish uchun kerak
     *
     * @param id  user id si keladi
     * @param dto orqali quyidagilar keladi
     *            <p>
     *            private String fullName;
     *            <p>
     *            private String username;
     *            <p>
     *            private String password;
     *            <p>
     *            private String prePassword;
     *            <p>
     *            private Long divisionId;
     *            <p>
     *            private Long roleId;
     * @return ApiResult orqali natija qaytadi
     * @PreAuthorize orqali user permissioni (huquqi) tekshiriladi
     * @Valid orqali json qiymatlar to'g'ri kelganligi tekshiriladi
     */

//    @PreAuthorize(value = "hasAuthority('EDIT_USER')")
    @RoleniTekshirish(role = "ADMIN")
    @PutMapping("/{id}")
    public HttpEntity<?> editUser(@PathVariable Long id, @Valid @RequestBody RegisterDto dto) {
        ApiResult apiResult = service.edit(id, dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }


    /**
     * Bu funksiya orqali user o'zining ma'lumotlarini o'zgartirishi mumkin
     *
     * @param user
     * @param dto  login, hozirgi parol, o'zgartirilayotgan parol, preParol keladi;
     * @return
     */


    @RoleniTekshirish(role = "USER, ADMIN")
    @PutMapping
    public HttpEntity<?> editUserForUser(@CurrentUser Users user, @Valid @RequestBody EditUserDto dto) {
        ApiResult apiResult = service.editUserForUser(user, dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }


    /**
     * barcha userlar ro'yxatini olish uchun ishlatiladi
     *
     * @return ApiResult orqali natija qaytadi true or false
     */

    @RoleniTekshirish(role = "ADMIN")
    @GetMapping
    public HttpEntity<?> getAll() {
        List<Users> all = service.getAll();
        return ResponseEntity.status(!all.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(all);
    }


    /**
     * bitta user ro'yxatini olish uchun ishlatiladi
     *
     * @param id user id si keladi
     * @return user qaytadi
     */

    @RoleniTekshirish(role = "ADMIN, USER")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id) {
        Users one = service.getOne(id);
        return ResponseEntity.status(one != null ? HttpStatus.OK : HttpStatus.CONFLICT).body(one);
    }


    @RoleniTekshirish(role = "ADMIN, USER")
    @GetMapping("/role")
    public HttpEntity<?> getRole(@CurrentUser Users user) {
        Role role = user.getRole();
        return ResponseEntity.status(role != null ? HttpStatus.OK : HttpStatus.CONFLICT).body(role);
    }

    /**
     * bitta userni o'chirish uchun ishlatiladi
     * delete qilish uchun unga bog'liq barcha narsani o'chirish kerak
     *
     * @param id user id si keladi
     * @return ApiResult orqali natija qaytadi true or false
     */

//    @PreAuthorize(value = "hasAuthority('DELETE_USER')")
    @RoleniTekshirish(role = "ADMIN")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResult apiResult = service.delete(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    /**
     * Метод для выхода пользователя из системы.
     *
     * @param user Текущий пользователь, передаваемый через аннотацию @CurrentUser.
     */
    @PostMapping("/logout")
    public void logout(@CurrentUser Users user) {
        service.logout(user);
    }


    /**
     * Метод для активации или деактивации пользователя по его идентификатору.
     * Требует роли "ADMIN" или "SUPERADMIN" для доступа.
     *
     * @param id     Идентификатор пользователя для изменения статуса активации.
     * @param active Флаг активации (true - активировать, false - деактивировать).
     * @return ResponseEntity с HTTP статусом OK в случае успеха, иначе CONFLICT.
     */
    @RoleniTekshirish(role = "ADMIN")
    @PutMapping("/active")
    public ResponseEntity<?> active(@RequestParam Long id, @RequestParam boolean active) {
        ApiResult apiResult = service.active(id, active);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }



}


