package uz.auth.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action; // LOGIN_SUCCESS, LOGIN_FAILURE, LOGOUT
    private LocalDateTime timestamp;
    private String description;

    public AuditLog(String username, String action, LocalDateTime timestamp,String description) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
        this.description=description;
    }

    public AuditLog() {

    }



}
