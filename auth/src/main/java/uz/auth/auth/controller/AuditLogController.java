package uz.auth.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.auth.auth.entity.AuditLog;
import uz.auth.auth.repository.AuditLogRepository;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping("/list")
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

//    @GetMapping("/{id}")
//    public AuditLog getAuditLogById(@PathVariable Long id) {
//        return auditLogRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("AuditLog not found with id " + id));
//    }

    // Additional endpoints for filtering, pagination, etc., can be added here
}
