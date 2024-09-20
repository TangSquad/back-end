package backend.tangsquad.common.controller;

import backend.tangsquad.auth.service.OAuthService;
import backend.tangsquad.common.dto.request.LoginRequestDto;
import backend.tangsquad.common.dto.response.ApiResponse;
import backend.tangsquad.common.dto.response.JwtResponseDto;
import backend.tangsquad.common.service.AuthService;
import backend.tangsquad.common.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final OAuthService oAuthService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            String userPlatform = userService.getUserPlatform(loginRequestDto.getEmail());
            if (userPlatform != null) {
                throw new IllegalArgumentException("다른 플랫폼으로 가입한 사용자입니다.");
            }
            JwtResponseDto jwtResponseDto = authService.authenticateUser(loginRequestDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful.", jwtResponseDto));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid email or password. Please try again.", null));
        } catch (Exception e) {
            // 여기에 디버그 로그를 추가하여 예상치 못한 예외를 포착합니다.
            e.printStackTrace();  // 예외의 스택 트레이스를 출력합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred. Please try again later.", null));
        }
    }

    @PostMapping("/login/oauth")
    public ResponseEntity<ApiResponse<JwtResponseDto>> loginOAuth(@RequestParam String accessToken) {
        try {
            JwtResponseDto jwtResponseDto = oAuthService.kakaoLogin(accessToken);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful.", jwtResponseDto));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid email or password. Please try again.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred. Please try again later.", null));
        }
    }



    @Operation(summary = "토큰 재발급", description = "Refresh Token을 이용하여 Access Token을 재발급합니다. Authorization 헤더에 <Refresh Token> 형식으로 전달되어야 합니다.")
    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponseDto>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            // Bearer 토큰이 헤더에 "Bearer " 접두어로 포함되어 있는 경우를 처리
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7); // "Bearer " 부분을 제거
            }
            JwtResponseDto jwtResponseDto = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully.", jwtResponseDto));
        } catch (IllegalArgumentException e) {
            System.err.println("Error refreshing token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid refresh token. Please try again.", null));
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred. Please try again later.", null));
        }
    }
}
