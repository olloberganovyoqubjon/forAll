package uz.forall.appstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.appstore.entity.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}
