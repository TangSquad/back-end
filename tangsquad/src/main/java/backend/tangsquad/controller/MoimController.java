package backend.tangsquad.controller;


import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.MoimCreateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.dto.response.MoimReadResponse;
import backend.tangsquad.repository.MoimRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.MoimService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.swagger.global.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    private UserService userService;

    @Autowired
    public MoimController(MoimService moimService, MoimRepository moimRepository, UserRepository userRepository, UserService userService) {
        this.moimService = moimService;
        this.moimRepository = moimRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("")
    public Moim createMoim(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MoimCreateRequest request) {
        // Retrieve the current authenticated user's username
        Long userId = userDetails.getId();

        // Find the user by username
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create a new Moim instance
        Moim moim = new Moim();

        // Set the user for the moim instance«
        User user = userOptional.get();
        moim.setUser(user);

        // Set the moim details from the request
        moim.setMoimMembers(request.getMoimMembers());
        moim.setStartDate(request.getStartDate());
        moim.setEndDate(request.getEndDate());

        moim.setMoimName(request.getMoimName());
        moim.setMoimIntro(request.getMoimIntro());
        moim.setMoimContents(request.getMoimContents());
        moim.setMaxPeople(request.getMaxPeople());
        moim.setPrice(request.getPrice());
        moim.setLicenseLimit(request.getLicenseLimit());
        moim.setRegion(request.getRegion());

        moim.setAge(request.getAge());
        moim.setMood(request.getMood());

        // Save the moim instance
        moimRepository.save(moim);

        return moim;
    }

    @GetMapping("")
    public List<Moim> getMoims(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
        // Retrieve the current authenticated user's username
        Long userId = userDetails.getId();

        // Find the user by username
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        // Get the moim list for the user
        User user = userOptional.get();
        return moimService.getMoims(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public Moim updateMoim(@PathVariable("id") Long moimId,
                           @RequestBody MoimUpdateRequest request,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 현재 인증된 사용자의 ID를 가져옴
        Long userId = userDetails.getId();

        // 요청한 사용자가 올바른 사용자인지 확인 후 모임 업데이트
        return moimService.updateMoim(moimId, request, userId);
    }

    @DeleteMapping("/{moimId}/{currentUserId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #moimId, #currentUserId)")
    public void deleteMoim(@PathVariable Long moimId, @PathVariable Long currentUserId) {
        moimService.deleteMoim(moimId, currentUserId);
    }


}
