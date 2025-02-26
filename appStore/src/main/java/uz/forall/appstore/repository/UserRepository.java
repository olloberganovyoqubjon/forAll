package uz.forall.appstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.appstore.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserIdAndSoftware_Id(Long userId, UUID softwareId);
}
