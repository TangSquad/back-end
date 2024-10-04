package backend.tangsquad.common.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.dto.request.*;
import backend.tangsquad.common.dto.response.ApiResponse;
import backend.tangsquad.common.dto.response.JwtResponseDto;
import backend.tangsquad.common.dto.response.RegisterResponse;
import backend.tangsquad.common.dto.response.WithdrawResponse;
import backend.tangsquad.common.service.AuthService;
import backend.tangsquad.common.service.ProfileService;
import backend.tangsquad.common.service.UserService;
import backend.tangsquad.common.service.VerificationService;
import backend.tangsquad.logbook.dto.request.LogCreateRequest;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.service.LogbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API")
public class UserController {

    private final UserService userService;
    private final VerificationService verificationService;
    private final ProfileService profileService;
    private final AuthService authService;
    private final LogbookService logbookService;

//    @Operation(summary = "유저가 좋아요한 로그북을 불러오는 API", description = "좋아요한 로그북 불러오기", security = @SecurityRequirement(name = "AccessToken"))
//    @GetMapping("/likeLogbook")
//    public ResponseEntity<List<LogCreateRequest>> getLikeLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        // Fetch liked logbook IDs for the authenticated user
//        Optional<List<String>> likedLogbookIds = userService.getLikedLogbookIds(userDetails.getId());
//
//        if (likedLogbookIds.isPresent() && !likedLogbookIds.get().isEmpty()) {
//            // Convert List<String> to List<Long>
//            List<Long> logbookIdsAsLong = likedLogbookIds.get().stream()
//                    .map(Long::valueOf)  // Convert each String to Long
//                    .collect(Collectors.toList());
//
//            // Fetch Logbook entities based on the IDs
//            List<Logbook> likedLogbooks = userService.findLogbooksByIds(logbookIdsAsLong);
//
//            // Convert Logbooks to LogCreateRequest DTOs
//            List<LogCreateRequest> logCreateRequests = likedLogbooks.stream()
//                    .map(this::convertToLogCreateRequest)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(logCreateRequests);  // Return the list of LogCreateRequest DTOs
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // No liked logbooks found
//        }
//    }

    @Operation(summary = "유저가 좋아요한 로그북을 불러오는 API", description = "좋아요한 로그북 불러오기", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/likeLogbook")
    public ResponseEntity<List<Long>> getLikeLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Fetch liked logbook IDs for the authenticated user
        Optional<List<Long>> likedLogbookIds = userService.getLikedLogbookIds(userDetails.getId());

        if (likedLogbookIds.isPresent() && !likedLogbookIds.get().isEmpty()) {
            // Print out the list of liked logbook IDs as Strings
            return ResponseEntity.ok(likedLogbookIds.get());  // Return the list of String IDs directly
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // No liked logbooks found
        }
    }



    // Example method to convert Logbook to LogCreateRequest (adjust as necessary)
    private LogCreateRequest convertToLogCreateRequest(Logbook logbook) {
        LogCreateRequest request = new LogCreateRequest();
        // set fields from logbook to request
        return request;
    }


    @Operation(summary = "가입 전 이메일 중복 확인 API", description = "가입 전 이메일 중복 확인 API")
    @PostMapping("/check/email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@Valid @RequestBody EmailCheckRequest emailCheckRequest) {
        if (userService.isEmailExists(emailCheckRequest.getEmail())) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Email already registered.", false));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, "Email available.", true));
        }
    }

    @Operation(summary = "가입 전 전화번호 중복 확인 API", description = "가입 전 전화번호 중복 확인 API")
    @PostMapping("/check/phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@Valid @RequestBody PhoneRequest phoneRequest) {
        if (userService.isPhoneExists(phoneRequest.getPhoneNumber())) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Phone number already registered.", false));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number available.", true));
        }
    }

    @Operation(summary = "가입 전 닉네임 중복 확인 API", description = "가입 전 닉네임 중복 확인 API")
    @PostMapping("/check/nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(@Valid @RequestBody NicknameCheckRequest nicknameRequest) {
        if (userService.isNicknameExists(nicknameRequest.getNickname())) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Nickname already registered.", false));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(true, "Nickname available.", true));
        }
    }

    @Operation(summary = "회원가입 API", description = "회원가입 API 토큰을 반환합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponseDto>> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponse response = userService.registerUser(registerRequestDto);
        if (!response.isSuccess()){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, response.getMessage(), null));
        }
        JwtResponseDto jwtResponseDto = authService.authenticateUser(new LoginRequestDto(registerRequestDto.getEmail(), registerRequestDto.getPassword()));
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully.", jwtResponseDto));
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
        boolean isSuccess = profileService.setAdditionalInfo(userId, additionalInfoRequest.getNickname(), additionalInfoRequest.getProfileImage(), additionalInfoRequest.getOrganizationId(), additionalInfoRequest.getLevelId(), additionalInfoRequest.getCertificateImage());
        if (isSuccess) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Additional info added successfully.", true));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to add additional info.", false));
        }
    }

    @Operation(summary = "추가 정보 입력 여부 확인 API", description = "추가 정보 입력 여부를 확인합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/additional/check")
    public ResponseEntity<ApiResponse<Boolean>> checkAdditionalInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        boolean isAdditionalInfoSet = userService.isUserUpdated(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Additional info check successful.", isAdditionalInfoSet));
    }
}
