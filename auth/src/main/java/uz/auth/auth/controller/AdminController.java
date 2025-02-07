package uz.auth.auth.controller;

import uz.auth.auth.annotation.RoleniTekshirish;
import uz.auth.auth.entity.Users;
import uz.auth.auth.payload.dto.AdminDto;
import uz.auth.auth.payload.dto.AuthenticationResponse;
import uz.auth.auth.payload.dto.SignInRequest;
import uz.auth.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class AdminController {

    private AuthenticationService authenticationService;

    @Autowired
    public AdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RoleniTekshirish(role = "ADMIN")
    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminDto request) {
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<AuthenticationResponse> loginAdmin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.loginAdmin(request));
    }

    @RoleniTekshirish(role = "ADMIN")
    @GetMapping("numberOfUsers")
    public HttpEntity<?> numberOfUsers() {
        Map<Integer, String> listStringMap = authenticationService.numberOfUsers();
        return ResponseEntity.ok(listStringMap);
    }

    @RoleniTekshirish(role = "ADMIN")
    @GetMapping("listOfUsers/{rol}")
    public HttpEntity<?> listOfUsers(@PathVariable String rol) {
        List<Users> listStringMap = authenticationService.listOfUsers(rol);
        return ResponseEntity.ok(listStringMap);
    }
}
