package uz.forall.murojaatsocket.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.forall.murojaatsocket.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
