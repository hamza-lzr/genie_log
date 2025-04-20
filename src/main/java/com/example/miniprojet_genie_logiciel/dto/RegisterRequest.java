package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}

