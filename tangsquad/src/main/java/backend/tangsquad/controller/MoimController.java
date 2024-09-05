package backend.tangsquad.controller;


import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.Moim;
import backend.tangsquad.dto.request.DivingUpdateRequest;
import backend.tangsquad.dto.request.MoimCreateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.dto.response.DivingReadResponse;
import backend.tangsquad.dto.response.MoimCreateResponse;
import backend.tangsquad.dto.response.MoimReadResponse;
import backend.tangsquad.repository.MoimRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.MoimService;
import backend.tangsquad.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/moim")
@RestController
@Tag(name = "Moim", description = "모임 관련 API")
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

    // Create a new diving
    @PostMapping
    @Operation(
            summary = "모임 생성",
            description = "새로운 모임을 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "모임 생성 성공", content = @Content(schema = @Schema(implementation = MoimCreateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<MoimCreateRequest> createMoim(
            @RequestBody MoimCreateRequest moimCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            Moim moim = new Moim();

            moim.setUser(userDetails.getUser());
            moim.setMoimName(moimCreateRequest.getMoimName());
            moim.setMoimIntro(moimCreateRequest.getMoimIntro());
            moim.setMoimDetails(moimCreateRequest.getMoimDetails());
            moim.setAnonymous(moimCreateRequest.getAnonymous());
            moim.setLicenseLimit(moimCreateRequest.getLicenseLimit());
            moim.setExpense(moimCreateRequest.getExpense());
            moim.setAge(moimCreateRequest.getAge());
            moim.setLimitPeople(moimCreateRequest.getLimitPeople());
            moim.setLocationOne(moimCreateRequest.getLocationOne());
            moim.setLocationTwo(moimCreateRequest.getLocationTwo());
            moim.setLocationThree(moimCreateRequest.getLocationThree());
            moim.setMoodOne(moimCreateRequest.getMoodOne());
            moim.setMoodTwo(moimCreateRequest.getMoodTwo());

            // Save moim
            Moim savedMoim = moimService.save(moim);
            return ResponseEntity.status(HttpStatus.CREATED).body(moimCreateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WRONG");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("")
    @Operation(summary = "내 모임 불러오기", description = "나의 모임들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<MoimReadResponse>> getMyMoims(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = userDetails.getId();  // Use the authentica임ed user's ID

        try {
            // Retrieve the user's logs
            List<Moim> moims = moimService.getMoims(userId);

            // Map Diving entities to DivingReadResponse DTOs
            List<MoimReadResponse> moimReadResponses = moims.stream()
                    .map(moim -> new MoimReadResponse(
                            moim.getId(),
                            moim.getUser().getId(),
                            moim.getAnonymous(),
                            moim.getMoimName(),
                            moim.getMoimIntro(),
                            moim.getMoimDetails(),
                            moim.getAge(),
                            moim.getLimitPeople(),
                            moim.getLicenseLimit(),
                            moim.getLocationOne(),
                            moim.getLocationTwo(),
                            moim.getLocationTwo(),
                            moim.getExpense(),
                            moim.getMoodOne(),
                            moim.getMoodTwo()
                            ))
                    .collect(Collectors.toList());

            // Return the list of DivingReadResponse
            return ResponseEntity.ok(moimReadResponses);

        } catch (Exception e) {
            // Log the exception with details
            Logger logger = LoggerFactory.getLogger(MoimController.class);
            logger.error("Error retrieving logs for user ID: " + userId, e);

            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new MoimReadResponse()));  // Adjust response as needed
        }
    }

    @PutMapping("")
    @Operation(summary = "모임 수정하기", description = "나의 모임을 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<MoimReadResponse>> updateDiving(
            @RequestBody MoimUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        Long moimId = request.getId();

        // Call the service to update the diving
        Optional<Moim> updatedMoimOptional = Optional.ofNullable(moimService.updateMoim(moimId, request));
        // If the diving was updated successfully
        if (updatedMoimOptional.isPresent()) {
            Moim updatedMoim = updatedMoimOptional.get();

            // Check if the user IDs match
            if (updatedMoim.getUser() == null) {
                throw new IllegalStateException("User associated with diving is null");
            }
            if (!userDetailsImpl.getId().equals(updatedMoim.getUser().getId())) {
                throw new AccessDeniedException("User is not authorized to update this ");
            }

            // Convert the updated diving to a LogReadResponse
            MoimReadResponse moimReadResponse = new MoimReadResponse(
                    updatedMoim.getId(),
                    updatedMoim.getUser().getId(),
                    updatedMoim.getAnonymous(),
                    updatedMoim.getMoimName(),
                    updatedMoim.getMoimIntro(),
                    updatedMoim.getMoimDetails(),
                    updatedMoim.getAge(),
                    updatedMoim.getLimitPeople(),
                    updatedMoim.getLicenseLimit(),
                    updatedMoim.getLocationOne(),
                    updatedMoim.getLocationTwo(),
                    updatedMoim.getLocationThree(),
                    updatedMoim.getExpense(),
                    updatedMoim.getMoodOne(),
                    updatedMoim.getMoodTwo()
            );
            // Return the updated LogReadResponse in a list (to maintain consistency with previous GET mapping)
            return ResponseEntity.ok(Collections.singletonList(moimReadResponse));
        } else {
            // Return a NOT_FOUND status if the diving could not be found or updated
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @GetMapping("")
//    public List<Moim> getMoims(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        this.userDetails = userDetails;
//        // Retrieve the current authenticated user's username
//        Long userId = userDetails.getId();
//
//        // Find the user by username
//        Optional<User> userOptional = userService.findById(userId);
//        if (!userOptional.isPresent()) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        // Get the moim list for the user
//        User user = userOptional.get();
//        return moimService.getMoims(user);
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
//    public Moim updateMoim(@PathVariable("id") Long moimId,
//                           @RequestBody MoimUpdateRequest request,
//                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        // 현재 인증된 사용자의 ID를 가져옴
//        Long userId = userDetails.getId();
//
//        // 요청한 사용자가 올바른 사용자인지 확인 후 모임 업데이트
//        return moimService.updateMoim(moimId, request, userId);
//    }
//
//    @DeleteMapping("/{moimId}/{currentUserId}")
//    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #moimId, #currentUserId)")
//    public void deleteMoim(@PathVariable Long moimId, @PathVariable Long currentUserId) {
//        moimService.deleteMoim(moimId, currentUserId);
//    }


}
