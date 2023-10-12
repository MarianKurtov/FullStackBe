package com.example.fullstackbackend.services;

import com.example.fullstackbackend.entities.RoleEntity;
import com.example.fullstackbackend.repository.RoleRepository;
import com.example.fullstackbackend.repository.UserRepository;
import com.example.fullstackbackend.security.AuthenticationRequest;
import com.example.fullstackbackend.security.AuthenticationResponse;
import com.example.fullstackbackend.security.RegisterRequest;
import com.example.fullstackbackend.entities.UserEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder encoder, JwtService jwtService, RoleRepository roleRepository, AuthenticationManager authenticationManager) {
        this.userRepository = repository;
        this.passwordEncoder = encoder;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            Set<RoleEntity> userRoles =  new HashSet<>();
            RoleEntity role = roleRepository.findByName("USER");
            userRoles.add(role);

            var user =  new UserEntity(
                    request.getName(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getEmail(),
                    userRoles
            );

            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(jwtToken);
        }
        else {
            return new AuthenticationResponse();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        //if mail and pass are correct
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
