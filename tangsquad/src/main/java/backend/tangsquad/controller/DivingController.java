package backend.tangsquad.controller;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.DivingCreateRequest;
import backend.tangsquad.dto.request.DivingUpdateRequest;
import backend.tangsquad.dto.response.DivingReadResponse;
import backend.tangsquad.repository.DivingRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.DivingService;
import backend.tangsquad.swagger.global.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/diving")
@RestController

public class DivingController {
    private final DivingService divingService;

    private final DivingRepository divingRepository;

    private final UserRepository userRepository;

    @Autowired
    public DivingController(DivingService divingService, DivingRepository divingRepository, UserRepository userRepository) {
        this.divingService = divingService;
        this.divingRepository = divingRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public Diving createDiving(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DivingCreateRequest request) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create a new Diving instance
        Diving diving = new Diving();

        // Set the user for the diving instance
        User user = userOptional.get();
        diving.setUser(user);

        // Set the diving details from the request
        // diving.setDivingName(request.getDivingName());
        // diving.setAge(request.getAge());
        // diving.setDivingContents(request.getDivingContents());
        // diving.setDivingIntro(request.getDivingIntro());
        // diving.setDivingMembers(request.getDivingMembers());

        // Save the diving instance
        divingRepository.save(diving);

        return diving;
    }


    @GetMapping("")
    public List<Diving> getDivings(@AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Get the diving list for the user
        User user = userOptional.get();
        return divingService.getDivings(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public Diving updateDiving(@PathVariable("id") Long id, @RequestBody DivingUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Update the diving details
        return divingService.updateDiving(username, id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public CommonResponse<DivingReadResponse> deleteDiving(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Delete the diving instance
        divingService.deleteDiving(username, id);

        return CommonResponse.success();
    }
}
