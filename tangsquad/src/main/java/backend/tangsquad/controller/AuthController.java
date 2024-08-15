package backend.tangsquad.controller;

import backend.tangsquad.dto.request.LoginRequestDto;
import backend.tangsquad.dto.response.JwtResponseDto;
import backend.tangsquad.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            JwtResponseDto jwtResponseDto = authService.authenticateUser(loginRequestDto);
            return ResponseEntity.ok(jwtResponseDto);
        } catch (AuthenticationException e) {
            // 로그인 실패 시 401 상태 코드와 함께 메시지를 반환
            return ResponseEntity.status(401).body("Invalid email or password. Please try again.");
        } catch (Exception e) {
            // 기타 예외 발생 시 500 상태 코드와 함께 메시지를 반환
            return ResponseEntity.status(500).body("An unexpected error occurred. Please try again later.");
        }
    }


    @Operation(summary = "토큰 재발급", description = "Refresh Token을 이용하여 Access Token을 재발급합니다. Authorization 헤더에 <Refresh Token> 형식으로 전달되어야 합니다.")
    @GetMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            // Bearer 토큰이 헤더에 "Bearer " 접두어로 포함되어 있는 경우를 처리
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7); // "Bearer " 부분을 제거
            }
            JwtResponseDto jwtResponseDto = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(jwtResponseDto);
        } catch (IllegalArgumentException e) {
            System.err.println("Error refreshing token: " + e.getMessage());
            return ResponseEntity.status(401).body(null);
        }
    }
}
