package uz.auth.auth.dto;

import java.util.List;

public class UserResponse {
    private String username;
    private List<String> roles;

    public UserResponse(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}