package com.example.miniprojet_genie_logiciel.dto;


import com.example.miniprojet_genie_logiciel.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Role role;
    private String email;

}
