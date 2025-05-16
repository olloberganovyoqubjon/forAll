package uz.murojaat.appeal.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.murojaat.appeal.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
