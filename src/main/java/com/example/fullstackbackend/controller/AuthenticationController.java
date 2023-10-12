package com.example.fullstackbackend.controller;

import com.example.fullstackbackend.security.AuthenticationRequest;
import com.example.fullstackbackend.security.AuthenticationResponse;
import com.example.fullstackbackend.security.RegisterRequest;
import com.example.fullstackbackend.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.login(request));
    }
}
