package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.AuthenticationRequest;
import com.example.miniprojet_genie_logiciel.dto.AuthenticationResponse;
import com.example.miniprojet_genie_logiciel.dto.RegisterRequest;
import com.example.miniprojet_genie_logiciel.entities.Role;
import com.example.miniprojet_genie_logiciel.entities.User;
import com.example.miniprojet_genie_logiciel.repository.UserRepository;
import com.example.miniprojet_genie_logiciel.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            Optional<User> currentUserOpt = userRepository.findByEmail(userDetails.getUsername());
            if (currentUserOpt.isEmpty() || currentUserOpt.get().getRole() != Role.ADMIN) {
                throw new RuntimeException("Seul un ADMIN peut enregistrer un utilisateur");
            }
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user.getEmail());

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String jwtToken = jwtService.generateToken(user.getEmail());
        return new AuthenticationResponse(jwtToken);
    }
}

