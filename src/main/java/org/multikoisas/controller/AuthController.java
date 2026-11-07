package org.multikoisas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.config.TokenService;
import org.multikoisas.dto.AuthRequest;
import org.multikoisas.dto.AuthResponse;
import org.multikoisas.entities.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthRequest data) {
        // Empacota credenciais
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());

        // Verifica hash da senha contra o banco automaticamente via AuthorizationService
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Gera token
        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(token));

    }
}
