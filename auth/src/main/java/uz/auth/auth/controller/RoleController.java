package uz.auth.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.auth.auth.entity.Role;
import uz.auth.auth.repository.RoleRepository;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/list")
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }


}
