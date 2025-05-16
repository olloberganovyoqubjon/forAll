package uz.auth.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.auth.auth.dto.ApiResponse;
import uz.auth.auth.dto.PasswordChangeRequest;
import uz.auth.auth.dto.UserAddDTO;
import uz.auth.auth.entity.User;
import uz.auth.auth.exception.UsernameAlreadyExistsException;
import uz.auth.auth.repository.UserRepository;
import uz.auth.auth.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/insert")
    public User createUser(@RequestBody UserAddDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }


    // Endpoint to get a specific user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserAddDTO dto) {
        long startTime = System.currentTimeMillis();
        try {
            User updatedUser = userService.updateUser(id, dto);
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("updateUser took: " + elapsed + " ms");

            return ResponseEntity.ok(updatedUser);
        } catch (UsernameAlreadyExistsException e) {
            System.out.println("updateUser failed - Username exists: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        } catch (RuntimeException e) {
            System.out.println("updateUser failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PutMapping("/update/enabled/{id}")
    public ResponseEntity<?> changeUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        boolean enabled = request.getOrDefault("enabled", false);
        User user = optionalUser.get();
        user.setEnabled(enabled);
        userRepository.save(user);
        List<User> users=userRepository.findAllByDeletedFalse();
        if (enabled) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>("Foydalanuvchi aktivlashtirildi", users));
        }
        else
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>("Foydalanuvchi bloklandi", users));

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            System.out.println("softDeleteUser failed: User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = optionalUser.get();
        user.setDeleted(true);
        userRepository.save(user);
        System.out.println("softDeleteUser took: " + (System.currentTimeMillis() - startTime) + " ms");
        return ResponseEntity.ok("User deleted (soft)");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request, Authentication authentication) {
        String username = authentication.getName();
        try {
            userService.changePassword(username, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





}

