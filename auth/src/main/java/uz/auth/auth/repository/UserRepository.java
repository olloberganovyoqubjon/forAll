package uz.auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.auth.auth.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Faqat o'chirilmagan foydalanuvchilarni olish
    List<User> findAllByDeletedFalse();

    // Username orqali foydalanuvchini olish (deleted bo'lmaganlar orasidan)
    User findByUsernameAndDeletedFalse(String username);

    // Agar kerak bo‘lsa, division bo‘yicha filter
    List<User> findByDivisionIdAndDeletedFalse(Long divisionId);
    Optional<User> findByUsername(String username);
    Optional<User> findByRoleName(String roleName); // Role bo‘yicha izlash

}
