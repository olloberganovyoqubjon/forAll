package uz.forall.appstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.appstore.entity.Software;

import java.util.UUID;

public interface ProgramRepository extends JpaRepository<Software, UUID> {
}
