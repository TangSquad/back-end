package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.dto.request.*;
import backend.tangsquad.dto.response.ApiResponse;
import backend.tangsquad.dto.response.RegisterResponse;
import backend.tangsquad.dto.response.WithdrawResponse;
import backend.tangsquad.service.ProfileService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final ProfileService profileService;

    @Autowired
    public UserController(UserService userService, VerificationService verificationService, ProfileService profileService) {
        this.userService = userService;
        this.verificationService = verificationService;
        this.profileService = profileService;
    }

    @Operation(summary = "가입 전 이메일 중복 확인 API", description = "가입 전 이메일 중복 확인 API")
    @PostMapping("/check/email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@Valid @RequestBody EmailCheckRequest emailCheckRequest) {
        if (userService.isEmailExists(emailCheckRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Email already registered.", false));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, "Email available.", true));
        }
    }

    @Operation(summary = "가입 전 전화번호 중복 확인 API", description = "가입 전 전화번호 중복 확인 API")
    @PostMapping("/check/phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@Valid @RequestBody PhoneRequest phoneRequest) {
        if (userService.isPhoneExists(phoneRequest.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Phone number already registered.", false));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number available.", true));
        }
    }

    @Operation(summary = "가입 전 닉네임 중복 확인 API", description = "가입 전 닉네임 중복 확인 API")
    @PostMapping("/check/nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@Valid @RequestBody NicknameCheckRequest nicknameRequest) {
        if (userService.isNicknameExists(nicknameRequest.getNickname())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Nickname already registered.", false));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, "Nickname available.", true));
        }
    }

    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Boolean>> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponse response = userService.registerUser(registerRequestDto);
        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, response.getMessage(), false));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, response.getMessage(), true));
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
    public ResponseEntity<ApiResponse<Boolean>> sendPhoneVerificationCode(@Valid @RequestBody PhoneRequest phoneRequest) {
        // 전화번호가 이미 등록되어 있는지 확인
        if (userService.isPhoneExists(phoneRequest.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Phone number already registered.", false));
        }

        // 인증 코드 전송
        verificationService.sendPhoneVerificationCode(phoneRequest.getPhoneNumber());
        return ResponseEntity.ok(new ApiResponse<>(true, "Verification code sent successfully.", true));
    }

    @Operation(summary = "전화번호 인증 코드 확인 API", description = "사용자가 받은 인증 코드를 확인합니다.")
    @PostMapping("/verification/phone/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyPhoneCode(@RequestBody PhoneVerificationCode phoneVerificationCode) {
        boolean isVerified = verificationService.verifyPhoneCode(phoneVerificationCode.getPhoneNumber(), phoneVerificationCode.getCode());

        if (isVerified) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number verified successfully.", true));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid verification code.", false));
        }
    }

    @Operation(summary = "추가정보 입력 API", description = "추가정보를 입력합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @PostMapping("/additional")
    public ResponseEntity<ApiResponse<Boolean>> addAdditionalInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody AdditionalInfoRequest additionalInfoRequest) {
        Long userId = userDetails.getId();
        boolean isSuccess = profileService.setAdditionalInfo(userId, additionalInfoRequest.getNickname(), additionalInfoRequest.getOrganizationId(), additionalInfoRequest.getLevelId(), additionalInfoRequest.getCertificateImage());
        if (isSuccess) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Additional info added successfully.", true));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to add additional info.", false));
        }
    }
}
