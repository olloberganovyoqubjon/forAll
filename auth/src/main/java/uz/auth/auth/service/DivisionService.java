package uz.auth.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.auth.auth.dto.DivisionDTO;
import uz.auth.auth.entity.Division;
import uz.auth.auth.repository.DivisionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    public List<DivisionDTO> getAllDivisions() {
        return divisionRepository.findByDeleteFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Create a new division
    public DivisionDTO createDivision(DivisionDTO divisionDTO) {
        Division division = toEntity(divisionDTO);
        division.setDelete(false); // Ensure new divisions are not marked as deleted
        Division savedDivision = divisionRepository.save(division);
        return toDTO(savedDivision);
    }

    // Update an existing division
    public DivisionDTO updateDivision(Long id, DivisionDTO divisionDTO) {
        Division division = divisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Division not found with id: " + id));
        updateEntityFromDTO(division, divisionDTO);
        Division updatedDivision = divisionRepository.save(division);
        return toDTO(updatedDivision);
    }

    // Soft delete a division
    public void deleteDivision(Long id) {
        Division division = divisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Division not found with id: " + id));
        division.setDelete(true);
        divisionRepository.save(division);
    }

    // Convert Division entity to DTO
    private DivisionDTO toDTO(Division division) {
        DivisionDTO dto = new DivisionDTO();
        dto.setId(division.getId());
        dto.setName(division.getName());
        dto.setShortName(division.getShortName());
        dto.setIndex(division.getIndex());
        dto.setDelete(division.isDelete());
        return dto;
    }

    // Convert DTO to Division entity
    private Division toEntity(DivisionDTO divisionDTO) {
        Division division = new Division();
        division.setId(divisionDTO.getId());
        division.setName(divisionDTO.getName());
        division.setShortName(divisionDTO.getShortName());
        division.setIndex(divisionDTO.getIndex());
        division.setDelete(divisionDTO.isDelete());
        return division;
    }

    // Update existing Division entity from DTO
    private void updateEntityFromDTO(Division division, DivisionDTO divisionDTO) {
        if (divisionDTO.getName() != null) {
            division.setName(divisionDTO.getName());
        }
        division.setShortName(divisionDTO.getShortName()); // Can be null
        division.setIndex(divisionDTO.getIndex()); // Can be null
        division.setDelete(divisionDTO.isDelete());
    }
}