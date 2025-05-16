package uz.auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.auth.auth.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {


}
