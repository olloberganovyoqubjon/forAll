package uz.auth.auth.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Size(min = 5, max = 255, message = "firstName должен содержать от 5 до 255 символов")
    @NotBlank(message = "firstName не может быть пустыми")
    private String firstName;

    @Size(min = 5, max = 255, message = "lastName должен содержать от 5 до 255 символов")
    @NotBlank(message = "lastName не может быть пустыми")
    private String lastName;

    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    private Integer districtId;
}