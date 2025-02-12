package uz.forall.library.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNull;
import uz.forall.library.entity.Category;

import java.util.UUID;

@RepositoryRestResource(path = "categories")
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Override
    @RestResource(exported = false)
    void deleteById(@NonNull UUID uuid);

    @Override
    @RestResource(exported = false)
    void delete(@NonNull Category entity);
}
