package backend.tangsquad.certificate.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.certificate.dto.request.UserCertificateRequest;
import backend.tangsquad.certificate.dto.response.UserCertificateResponse;
import backend.tangsquad.certificate.entity.CertLevel;
import backend.tangsquad.certificate.entity.CertOrganization;
import backend.tangsquad.certificate.service.CertLevelService;
import backend.tangsquad.certificate.service.CertOrganizationService;
import backend.tangsquad.certificate.service.UserCertificateService;
import backend.tangsquad.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "자격증 관련 API", description = "자격증 등록 및 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class UserCertificateController {

    private final UserCertificateService userCertificateService;
    private final CertOrganizationService certOrganizationService;
    private final CertLevelService certLevelService;

    @Operation(summary = "사용자 자격증 조회", description = "사용자의 자격증을 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping()
    public ResponseEntity<ApiResponse<UserCertificateResponse>> getUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserCertificateResponse userCertificateResponse = userCertificateService.getUserCertificate(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 조회 성공", userCertificateResponse));
    }

    @Operation(summary = "사용자 자격증 삭제", description = "사용자의 자격증을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @DeleteMapping()
    public ResponseEntity<ApiResponse<String>> deleteUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userCertificateService.deleteUserCertificate(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 삭제 성공", null));
    }

    @Operation(summary = "사용자 자격증 등록", description = "사용자의 자격증을 등록합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @PostMapping()
    public ResponseEntity<ApiResponse<UserCertificateResponse>> createUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserCertificateRequest userCertificateRequest) {
        UserCertificateResponse userCertificateResponse = userCertificateService.createUserCertificate(userDetails.getId(), userCertificateRequest.getOrganizationId(), userCertificateRequest.getLevelId(), userCertificateRequest.getCertificateImage());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 등록 성공", userCertificateResponse));
    }

    @Operation(summary = "사용자 자격증 수정", description = "사용자의 자격증을 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @PutMapping()
    public ResponseEntity<ApiResponse<UserCertificateResponse>> updateUserCertificate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserCertificateRequest userCertificateRequest) {
        UserCertificateResponse userCertificateResponse = userCertificateService.updateUserCertificate(userDetails.getId(), userCertificateRequest.getOrganizationId(), userCertificateRequest.getLevelId(), userCertificateRequest.getCertificateImage());
        return ResponseEntity.ok(new ApiResponse<>(true, "사용자 자격증 수정 성공", userCertificateResponse));
    }

    @Operation(summary = "단체 리스트 조회", description = "단체 리스트를 조회합니다.")
    @GetMapping("/public/organization")
    public ResponseEntity<ApiResponse<List<CertOrganization>>> getOrganizationList() {
        List<CertOrganization> certOrganizationList = certOrganizationService.getAllCertOrganization();
        return ResponseEntity.ok(new ApiResponse<>(true, "단체 리스트 조회 성공", certOrganizationList));
    }

    @Operation(summary = "등급 리스트 조회", description = "단체의 등급 리스트를 조회합니다.")
    @GetMapping("/public/{organizationId}/level")
    public ResponseEntity<ApiResponse<List<CertLevel>>> getLevelList(@PathVariable Long organizationId) {
        List<CertLevel> certLevelList = certLevelService.getCertLevelByCertOrganizationId(organizationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "등급 리스트 조회 성공", certLevelList));
    }

}
