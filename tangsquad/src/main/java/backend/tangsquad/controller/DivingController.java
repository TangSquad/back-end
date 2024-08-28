package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.DivingCreateRequest;
import backend.tangsquad.dto.request.DivingUpdateRequest;
import backend.tangsquad.dto.response.DivingCreateResponse;
import backend.tangsquad.dto.response.DivingReadResponse;
import backend.tangsquad.service.DivingService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.swagger.global.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponse
@RequestMapping("/diving")
@RestController
public class DivingController {

    private final DivingService divingService;
    private final UserService userService; // Use UserService to handle user-related operations

    @Autowired
    public DivingController(DivingService divingService, UserService userService) {
        this.divingService = divingService;
        this.userService = userService;
    }

    @PostMapping("")
    public Diving createDiving(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody DivingCreateRequest request) {
        // Retrieve the current authenticated user's ID
        Long userId = userDetails.getId();

        // Create a new Diving instance and return the response
        return divingService.createDiving(userId, request);
    }

//    @GetMapping("")
//    public List<Diving> getDivings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        // Retrieve the current authenticated user's ID
//        Long userId = userDetails.getId();
//
//        // Get the diving list for the user
//        return divingService.getDivings(userId);
//    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public Diving updateDiving(@PathVariable("id") Long divingId, @RequestBody DivingUpdateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = userDetails.getId();

        // Update the diving details using the service layer
        return divingService.updateDiving(userId, divingId, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public CommonResponse<DivingReadResponse> deleteDiving(@PathVariable("id") Long divingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = userDetails.getId();

        // Delete the diving instance using the service layer
        divingService.deleteDiving(userId, divingId);

        return CommonResponse.success();
    }
}
