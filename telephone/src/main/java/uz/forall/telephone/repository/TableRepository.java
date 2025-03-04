package uz.forall.telephone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.forall.telephone.entity.Table;


@RepositoryRestResource(path = "tables")
public interface TableRepository extends JpaRepository<Table, Long> {
}
