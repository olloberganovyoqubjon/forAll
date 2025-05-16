package uz.auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.auth.auth.entity.ExceptionLogs;

public interface ExceptionLogsRepository extends JpaRepository<ExceptionLogs,Long> {

}
