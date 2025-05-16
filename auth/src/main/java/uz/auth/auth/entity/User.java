package uz.auth.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String fatherName;
    @Column(nullable = false, unique = true)
    private String username;

    private String phoneNumber;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private boolean deleted = false;
    private boolean active = true;

    @ManyToOne()
    @JoinColumn(name = "division_id")
    private Division division;


    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")  // RoleId orqali bog‘lanadi
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean accountNonExpired=true;
    private boolean enabled=true;
    private boolean credentialsNonExpired=true;
    private boolean accountNonLocked=true;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // yoki ROLE_ADMIN
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Dynamically fetching the role name and prefixing with "ROLE_"
        return List.of(new SimpleGrantedAuthority(this.role.getName())); // Assuming `role.getName()` returns "USER" or "ADMIN"
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // accountni bloklash va boshqalar
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked; // aktiv bo'lmasa login bo'lmaydi
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    // Getters and setters
}

