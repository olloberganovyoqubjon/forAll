package uz.auth.auth.dto;
import lombok.Data;

@Data
public class UserAddDTO {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String password;
    private Long roleId;  // RoleId maydoni qo‘shildi
    private String username;
    private String phoneNumber;
    private boolean enabled;
    private Long divisionId;
}
