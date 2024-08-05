package backend.tangsquad.controller;

import backend.tangsquad.dto.response.UserEquipmentResponse;
import backend.tangsquad.dto.response.UserIntroductionResponse;
import backend.tangsquad.dto.response.UserProfileResponse;
import backend.tangsquad.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        UserProfileResponse userProfileResponse = profileService.getUserProfile(id);
        return ResponseEntity.ok(userProfileResponse);
    }

    @GetMapping("/{id}/introduction")
    public ResponseEntity<UserIntroductionResponse> getUserIntroduction(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getUserIntroduction(id));
    }

    @GetMapping("/{id}/equipment")
    public ResponseEntity<UserEquipmentResponse> getUserEquipment(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getUserEquipment(id));
    }


}
