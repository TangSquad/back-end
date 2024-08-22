package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.dto.response.ApiResponse;
import backend.tangsquad.dto.response.UserEquipmentResponse;
import backend.tangsquad.dto.response.UserIntroductionResponse;
import backend.tangsquad.dto.response.UserProfileResponse;
import backend.tangsquad.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "Profile", description = "Profile of the user")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "유저 프로필 조회", description = "유저의 프로필을 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileResponse userProfileResponse = profileService.getUserProfile(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "User profile retrieved successfully.", userProfileResponse));
    }

    @Operation(summary = "유저 소개 조회", description = "유저의 소개를 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/introduction")
    public ResponseEntity<ApiResponse<UserIntroductionResponse>> getUserIntroduction(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserIntroductionResponse userIntroductionResponse = profileService.getUserIntroduction(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "User introduction retrieved successfully.", userIntroductionResponse));
    }

    @Operation(summary = "유저 장비 조회", description = "유저의 장비 정보를 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/equipment")
    public ResponseEntity<ApiResponse<UserEquipmentResponse>> getUserEquipment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserEquipmentResponse userEquipmentResponse = profileService.getUserEquipment(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "User equipment retrieved successfully.", userEquipmentResponse));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse userProfileResponse = profileService.getUserProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User profile retrieved successfully.", userProfileResponse));
    }

    @GetMapping("/introduction/{userId}")
    public ResponseEntity<ApiResponse<UserIntroductionResponse>> getUserIntroduction(@PathVariable Long userId) {
        UserIntroductionResponse userIntroductionResponse = profileService.getUserIntroduction(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User introduction retrieved successfully.", userIntroductionResponse));
    }

    @GetMapping("/equipment/{userId}")
    public ResponseEntity<ApiResponse<UserEquipmentResponse>> getUserEquipment(@PathVariable Long userId) {
        UserEquipmentResponse userEquipmentResponse = profileService.getUserEquipment(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User equipment retrieved successfully.", userEquipmentResponse));
    }
}
