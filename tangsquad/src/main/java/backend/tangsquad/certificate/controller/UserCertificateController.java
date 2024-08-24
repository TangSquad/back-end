package backend.tangsquad.certificate.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.certificate.dto.request.UserCertificateRequest;
import backend.tangsquad.certificate.dto.response.UserCertificateResponse;
import backend.tangsquad.certificate.service.UserCertificateService;
import backend.tangsquad.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "자격증 관련 API", description = "자격증 등록 및 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class UserCertificateController {

    private final UserCertificateService userCertificateService;

    @Operation(summary = "사용자 자격증 조회", description = "사용자의 자격증을 조회합니다.")
    @GetMapping()
    public ResponseEntity<ApiResponse<UserCertificateResponse>> getUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserCertificateResponse userCertificateResponse = userCertificateService.getUserCertificate(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 조회 성공", userCertificateResponse));
    }

    @Operation(summary = "사용자 자격증 삭제", description = "사용자의 자격증을 삭제합니다.")
    @DeleteMapping()
    public ResponseEntity<ApiResponse<String>> deleteUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userCertificateService.deleteUserCertificate(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 삭제 성공", null));
    }

    @Operation(summary = "사용자 자격증 등록", description = "사용자의 자격증을 등록합니다.")
    @PostMapping()
    public ResponseEntity<ApiResponse<UserCertificateResponse>> createUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserCertificateRequest userCertificateRequest) {
        UserCertificateResponse userCertificateResponse = userCertificateService.createUserCertificate(userDetails.getId(), userCertificateRequest.getOrganizationId(), userCertificateRequest.getLevelId(), userCertificateRequest.getCertificateImage());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 등록 성공", userCertificateResponse));
    }

    @Operation(summary = "사용자 자격증 수정", description = "사용자의 자격증을 수정합니다.")
    @PutMapping()
    public ResponseEntity<ApiResponse<UserCertificateResponse>> updateUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserCertificateRequest userCertificateRequest) {
        UserCertificateResponse userCertificateResponse = userCertificateService.updateUserCertificate(userDetails.getId(), userCertificateRequest.getOrganizationId(), userCertificateRequest.getLevelId(), userCertificateRequest.getCertificateImage());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 수정 성공", userCertificateResponse));
    }

}
