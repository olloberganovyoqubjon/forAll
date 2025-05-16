package uz.auth.auth.service;

import org.springframework.stereotype.Service;
import uz.auth.auth.entity.ExceptionLogs;
import uz.auth.auth.repository.ExceptionLogsRepository;

import java.util.List;

@Service
public class ExceptionLogsService {

    private final ExceptionLogsRepository exceptionLogsRepository;

    public ExceptionLogsService(ExceptionLogsRepository exceptionLogsRepository) {
        this.exceptionLogsRepository = exceptionLogsRepository;
    }

    public List<ExceptionLogs> getAllExceptions(){
        return exceptionLogsRepository.findAll();
    }

}
