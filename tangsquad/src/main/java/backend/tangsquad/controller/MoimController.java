package backend.tangsquad.controller;


import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.MoimCreateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.dto.response.MoimReadResponse;
import backend.tangsquad.repository.MoimRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.MoimService;
import backend.tangsquad.swagger.global.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/moim")
@RestController
public class MoimController {

    private final MoimService moimService;

    private final MoimRepository moimRepository;

    private final UserRepository userRepository;
    private UserDetails userDetails;

    @Autowired
    public MoimController(MoimService moimService, MoimRepository moimRepository, UserRepository userRepository) {
        this.moimService = moimService;
        this.moimRepository = moimRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public Moim createMoim(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MoimCreateRequest request) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create a new Moim instance
        Moim moim = new Moim();

        // Set the user for the moim instance
        User user = userOptional.get();
        moim.setUser(user);

        // Set the moim details from the request
        moim.setMoimName(request.getMoimName());
        moim.setAge(request.getAge());
        moim.setMoimContents(request.getMoimContents());
        moim.setMoimIntro(request.getMoimIntro());
        moim.setMoimMembers(request.getMoimMembers());

        // Save the moim instance
        moimRepository.save(moim);

        return moim;
    }

    @GetMapping("")
    public List<Moim> getMoims(@AuthenticationPrincipal UserDetails userDetails) {
        this.userDetails = userDetails;
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Get the moim list for the user
        User user = userOptional.get();
        return moimService.getMoims(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public Moim updateMoim(@PathVariable("id") Long id, @RequestBody MoimUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Update the moim details
        return moimService.updateMoim(username, id, request);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public CommonResponse<MoimReadResponse> deleteMoim(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the current authenticated user's username
        String username = userDetails.getUsername();

        // Delete the moim instance
        moimService.deleteMoim(username, id);

        return CommonResponse.success();
    }

}
