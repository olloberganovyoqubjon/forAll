package uz.auth.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import uz.auth.auth.entity.AuditLog;
import uz.auth.auth.repository.AuditLogRepository;

import java.time.LocalDateTime;

@Component
public class AuthenticationAuditListener {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        String description = "Foydalanuvchi tizimga muvaffaqiyatli kirdi.";

        auditLogRepository.save(new AuditLog(auth.getName(), "LOGIN_SUCCESS",LocalDateTime.now(),description));
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        String description = "Foydalanuvchi tizimga kira olmadi.";
        auditLogRepository.save(new AuditLog(username, "LOGIN_FAILURE",LocalDateTime.now(),description));
    }

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        if (auth != null) {
            String description = "Foydalanuvchi tizimdan chiqdi.";

            auditLogRepository.save(new AuditLog(auth.getName(), "LOGOUT", LocalDateTime.now(),description));
        }
    }
}
