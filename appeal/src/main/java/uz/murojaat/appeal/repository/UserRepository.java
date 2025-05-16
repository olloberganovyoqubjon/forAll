package uz.murojaat.appeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.murojaat.appeal.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByOrganization_Id(Long organizationId);

    List<User> findUserByIsOrderly(Boolean isOrderly);

    List<User> findUserByOrganization_IdAndIsOrderly(Long organizationId, Boolean isOrderly);
}
