package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.UserDTO;
import com.example.miniprojet_genie_logiciel.entities.Role;
import com.example.miniprojet_genie_logiciel.entities.User;
import com.example.miniprojet_genie_logiciel.mapper.UserMapper;
import com.example.miniprojet_genie_logiciel.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(rawPassword));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
                Optional<User> currentUser = userRepository.findByEmail(userDetails.getUsername());
                return currentUser.isPresent() && currentUser.get().getRole() == Role.ADMIN;
            }
        }
        return false;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUserDTO);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO updatedUserDTO) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(updatedUserDTO.getFirstName());
            existingUser.setLastName(updatedUserDTO.getLastName());
            existingUser.setEmail(updatedUserDTO.getEmail());
            existingUser.setRole(updatedUserDTO.getRole());
            User updatedUser = userRepository.save(existingUser);
            return userMapper.toUserDTO(updatedUser);
        });
    }
}
