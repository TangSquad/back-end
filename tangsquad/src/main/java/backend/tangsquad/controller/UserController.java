package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.dto.request.RegisterCheckRequest;
import backend.tangsquad.dto.request.RegisterRequestDto;
import backend.tangsquad.dto.response.ApiResponse;
import backend.tangsquad.dto.response.RegisterResponse;
import backend.tangsquad.dto.response.WithdrawResponse;
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
    private final VerificationService verificationService;

    @Autowired
    public UserController(UserService userService, VerificationService verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }

    @Operation(summary = "가입 전 중복(이메일, 사용자 이름, 전화번호) 확인 API", description = "가입 전 중복(이메일, 사용자 이름, 전화번호) 확인 API")
    @PostMapping("/registerValidationCheck")
    public ResponseEntity<ApiResponse<String>> registerValidationCheck(@Valid @RequestBody RegisterCheckRequest registerRequestDto) {
        String result = userService.registerValidationCheck(registerRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Validation check completed successfully.", result));
    }

    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponse response = userService.registerUser(registerRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully.", response));
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<WithdrawResponse>> withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        WithdrawResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User withdrawn successfully.", response));
    }

    @Operation(summary = "전화번호 인증 코드 전송 API", description = "회원 가입 전 전화번호로 인증 코드를 전송합니다.")
    @PostMapping("/verification/phone/send")
    public ResponseEntity<ApiResponse<String>> sendPhoneVerificationCode(@RequestParam String phoneNumber) {
        // 전화번호가 이미 등록되어 있는지 확인
        if (userService.isPhoneExists(phoneNumber)) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Phone number already registered."));
        }

        // 인증 코드 전송
        verificationService.sendPhoneVerificationCode(phoneNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Verification code sent successfully."));
    }

    @Operation(summary = "전화번호 인증 코드 확인 API", description = "사용자가 받은 인증 코드를 확인합니다.")
    @PostMapping("/verification/phone/verify")
    public ResponseEntity<ApiResponse<String>> verifyPhoneCode(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean isVerified = verificationService.verifyPhoneCode(phoneNumber, code);

        if (isVerified) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number verified successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid verification code."));
        }
    }
}
