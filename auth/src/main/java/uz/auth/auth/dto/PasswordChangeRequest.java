package uz.auth.auth.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;

    // Getters and setters
}
