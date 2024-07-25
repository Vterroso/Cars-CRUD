package com.vterroso.carregistry.service.impl;


import com.vterroso.carregistry.controller.dto.LoginRequest;
import com.vterroso.carregistry.controller.dto.LoginResponse;
import com.vterroso.carregistry.controller.dto.SingUpRequest;
import com.vterroso.carregistry.repository.UserRepository;
import com.vterroso.carregistry.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse signup(SingUpRequest request) {
        var user = UserEntity
                .builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_CLIENT")
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return LoginResponse.builder().jwt(jwt).build();
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        var jwt = jwtService.generateToken(user);
        return LoginResponse.builder().jwt(jwt).build();
    }

}
