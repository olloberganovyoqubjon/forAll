package uz.murojaat.appeal.service;

import org.springframework.stereotype.Service;
import uz.murojaat.appeal.model.Organization;
import uz.murojaat.appeal.payload.ApiResult;
import uz.murojaat.appeal.repository.OrganizationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public ApiResult add(Organization organization) {
        try {
            Organization savedOrganization = new Organization();
            savedOrganization.setName(organization.getName());
            organizationRepository.save(savedOrganization);
            return new ApiResult("success",true);
        } catch (Exception e) {
            return new ApiResult("error",false);
        }
    }


    public ApiResult edit(Organization organization, Long id) {
        try {
            Optional<Organization> optionalOrganization = organizationRepository.findById(id);
            Organization savedOrganization;
            if (optionalOrganization.isPresent()) {
                savedOrganization = optionalOrganization.get();
                savedOrganization.setName(organization.getName());
                organizationRepository.save(savedOrganization);
            } else {
                return new ApiResult("Bunday organizatsiya topilmadi",false);
            }
            return new ApiResult("success",true);
        } catch (Exception e) {
            return new ApiResult("error",false);
        }
    }

    public ApiResult get() {
        try {
            List<Organization> organizations = organizationRepository.findAll();
            return new ApiResult("success",true,organizations);
        } catch (Exception e) {
            return new ApiResult("error",false);
        }
    }

    public ApiResult delete(Long id) {
        try {
            organizationRepository.deleteById(id);
            return new ApiResult("success",true);
        } catch (Exception e){
            return new ApiResult("error",false);
        }
    }
}
