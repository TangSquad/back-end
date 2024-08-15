package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.RegisterCheckRequest;
import backend.tangsquad.dto.request.RegisterRequestDto;
import backend.tangsquad.dto.response.RegisterResponse;
import backend.tangsquad.dto.response.WithdrawResponse;
import backend.tangsquad.service.AuthService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, VerificationService verificationService) {
        this.userService = userService;
    }

    @Operation(summary = "가입 전 중복(이메일, 사용자 이름, 전화번호) 확인 API" , description = "가입 전 중복(이메일, 사용자 이름, 전화번호) 확인 API")
    @PostMapping("/registerValidationCheck")
    public String registerValidationCheck(@Valid @RequestBody RegisterCheckRequest registerRequestDto) {
        return userService.registerValidationCheck(registerRequestDto);
    }

    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponse response = userService.registerUser(registerRequestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API")
    @DeleteMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        WithdrawResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(response);
    }
}
