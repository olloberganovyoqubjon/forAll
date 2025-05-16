package uz.auth.auth.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.auth.auth.dto.DivisionDTO;
import uz.auth.auth.service.DivisionService;

import java.util.Map;

@RestController
@RequestMapping("/api/divisions")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        return Map.of("divisions", divisionService.getAllDivisions());
    }

    @PostMapping("/insert")
    public DivisionDTO insert(@RequestBody DivisionDTO divisionDTO) {
        return divisionService.createDivision(divisionDTO);
    }

    @PutMapping("/update/{id}")
    public DivisionDTO update(@PathVariable Long id, @RequestBody DivisionDTO divisionDTO) {
        return divisionService.updateDivision(id, divisionDTO);
    }


    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteDivision(@PathVariable Long id) {
        try {
            divisionService.deleteDivision(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Division not found with id: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete division: " + e.getMessage()));
        }
    }
}