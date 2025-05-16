package uz.auth.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.auth.auth.entity.ExceptionLogs;
import uz.auth.auth.repository.ExceptionLogsRepository;

import java.util.List;


@RestController
@RequestMapping("/api/error-logs")
public class ExceptionLogController {
    @Autowired
    private ExceptionLogsRepository exceptionLogsRepository;

    @GetMapping("/list")
    public List<ExceptionLogs> getAllAuditLogs() {
        return exceptionLogsRepository.findAll();
    }


}
