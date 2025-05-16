package uz.auth.auth.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.auth.auth.dto.LoginRequest;
import uz.auth.auth.dto.UserResponse;
import uz.auth.auth.entity.User;
import uz.auth.auth.jwt.JwtUtils;
import uz.auth.auth.repository.UserRepository;
import uz.auth.auth.service.CustomUserDetailsService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for username: {}", loginRequest.getUsername());

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Load the user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
// Load user entity from DB
        User user = (User) authentication.getPrincipal();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(userDetails);

        // Create user response
        UserResponse userResponse = new UserResponse(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .collect(Collectors.toList())
        );

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("serviceToken", jwt);
        response.put("user", userResponse);

        logger.info("Login successful for username: {}", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getPrincipal() instanceof UserDetails
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : "unknown";
        logger.info("Logout attempt for username: {}", username);

        // JWT is stateless, so no server-side invalidation is needed
        // Client will clear token via useAuth logout
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");

        logger.info("Logout successful for username: {}", username);

        // Manually publish the logout event
        eventPublisher.publishEvent(new LogoutSuccessEvent(authentication));

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/me")
//    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
//        // Get the current user from the security context
//        User currentUser = (User) authentication.getPrincipal();
//        return ResponseEntity.ok(currentUser);
//    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        logger.info("Processing /api/account/me");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
            logger.error("User not authenticated for /api/account/me");
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("Fetching details for user: {}", userDetails.getUsername());

        UserResponse userResponse = new UserResponse(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .collect(Collectors.toList())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("user", userResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        String usernameFromJwtToken = jwtUtils.getUsernameFromJwtToken(token);
        if (usernameFromJwtToken != null) {
            Optional<User> optionalUser = userRepository.findByUsername(usernameFromJwtToken);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
            }
            User user = optionalUser.get();
            Map<String, Long> response = new HashMap<>();
            response.put("userId", user.getId()); // Haqiqiy userId qo'yiladi
            return ResponseEntity.ok().body(response);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
    }
}