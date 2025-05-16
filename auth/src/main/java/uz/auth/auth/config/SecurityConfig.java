package uz.auth.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.auth.auth.jwt.JwtAuthenticationFilter;
import uz.auth.auth.jwt.JwtUtils;
import uz.auth.auth.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(authenticationManager, jwtUtils, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No server-side sessions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/account/login","/api/account/validate/**").permitAll()
                        .requestMatchers("/api/account/me", "/api/account/logout").authenticated()
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .addFilterBefore(jwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))),
                        UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }
//    @Bean
//    public CommandLineRunner initSuperAdmin(
//            @Value("${superadmin.username}") String username,
//            @Value("${superadmin.password}") String password,
//            UserRepository userRepository,
//            RoleRepository roleRepository,
//            PasswordEncoder passwordEncoder
//    ) {
//        return args -> {
//            if (userRepository.findByUsername(username).isEmpty()) {
//                // Create role if not exists
//                Role role = roleRepository.findByName("ROLE_SUPERADMIN");
//                if (role == null) {
//                    role = new Role();
//                    role.setName("ROLE_SUPERADMIN");
//                    role = roleRepository.save(role);
//                }
//
//                // Create superadmin user
//                User user = new User();
//                user.setUsername(username);
//                user.setPassword(passwordEncoder.encode(password));
//                user.setActive(true);
//                user.setDeleted(false);
//                user.setRole(role);
//                userRepository.save(user);
//
//                System.out.println("✅ Super Admin created: " + username);
//            }
//        };
//    }

}