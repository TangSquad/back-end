package backend.tangsquad.controller;

import backend.tangsquad.dto.request.LoginRequestDto;
import backend.tangsquad.dto.request.RefreshTokenRequestDto;
import backend.tangsquad.dto.response.JwtResponseDto;
import backend.tangsquad.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            JwtResponseDto jwtResponseDto = authService.authenticateUser(loginRequestDto);
            return ResponseEntity.ok(jwtResponseDto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshToken) {
        try {
            JwtResponseDto jwtResponseDto = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(jwtResponseDto);
        } catch (IllegalArgumentException e) {
            System.err.println("Error refreshing token: " + e.getMessage());
            return ResponseEntity.status(401).body(null);
        }
    }
}
