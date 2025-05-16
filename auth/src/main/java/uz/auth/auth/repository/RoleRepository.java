package uz.auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.auth.auth.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

        Optional<Role> findById(Long id);  // RoleId orqali izlash

        Role findByName(String name);


}

