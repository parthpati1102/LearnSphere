package com.learnsphere.Learnsphere.dto;
import lombok.Data;
import com.learnsphere.Learnsphere.model.Role;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}