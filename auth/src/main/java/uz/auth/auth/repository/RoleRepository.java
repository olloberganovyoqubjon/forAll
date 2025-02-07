package uz.auth.auth.repository;

import uz.auth.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Этот код определяет интерфейс RoleRepository, который является частью пакета fan.company.filetransfer.repository.
     * Он используется для управления сущностью Role в базе данных с помощью JPA (Java Persistence API). Интерфейс наследует
     * JpaRepository, что предоставляет стандартные методы для работы с базой данных (например, сохранение, удаление, поиск по ID и т.д.).
     *
     * Дополнительно в интерфейсе определен кастомный запрос с использованием аннотации @Query, который выбирает все роли,
     * за исключением роли с именем "SUPERADMIN".
     */

    // Кастомный запрос для поиска всех ролей, за исключением роли с именем "SUPERADMIN"
    @Query("SELECT e FROM Role e WHERE e.name<>'SUPERADMIN'")
    List<Role> findAllWithoutSuperAdmin();
    List<Role> findByName(String name);
}
