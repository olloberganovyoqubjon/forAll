package uz.forall.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.notes.eintity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(Long userId);
}
