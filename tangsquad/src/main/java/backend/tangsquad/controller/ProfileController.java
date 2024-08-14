package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
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
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileResponse userProfileResponse = profileService.getUserProfile(userDetails.getId());
        return ResponseEntity.ok(userProfileResponse);
    }

    @Operation(summary = "유저 소개 조회", description = "유저의 소개를 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/introduction")
    public ResponseEntity<UserIntroductionResponse> getUserIntroduction(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.getUserIntroduction(userDetails.getId()));
    }

    @Operation(summary = "유저 장비 조회", description = "유저의 장비 정보를 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("/equipment")
    public ResponseEntity<UserEquipmentResponse> getUserEquipment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.getUserEquipment(userDetails.getId()));
    }


    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse userProfileResponse = profileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/introduction/{userId}")
    public ResponseEntity<UserIntroductionResponse> getUserIntroduction(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getUserIntroduction(userId));
    }

    @GetMapping("/equipment/{userId}")
    public ResponseEntity<UserEquipmentResponse> getUserEquipment(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getUserEquipment(userId));
    }


}
