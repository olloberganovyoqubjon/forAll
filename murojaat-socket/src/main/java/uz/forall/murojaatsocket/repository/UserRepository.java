package uz.forall.murojaatsocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.murojaatsocket.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByOrganization_Id(Long organizationId);
}
