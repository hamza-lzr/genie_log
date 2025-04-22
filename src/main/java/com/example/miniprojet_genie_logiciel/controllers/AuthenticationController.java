package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.AuthenticationRequest;
import com.example.miniprojet_genie_logiciel.dto.AuthenticationResponse;
import com.example.miniprojet_genie_logiciel.dto.RegisterRequest;
import com.example.miniprojet_genie_logiciel.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Tu peux restreindre si besoin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

