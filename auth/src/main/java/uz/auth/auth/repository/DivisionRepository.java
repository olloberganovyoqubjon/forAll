package uz.auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.auth.auth.entity.Division;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {


    // Derived query method
    List<Division> findByDeleteFalse();

    // Alternative explicit query
    @Query("SELECT d FROM Division d WHERE d.delete = false")
    List<Division> findAllNonDeleted();
}

