package backend.tangsquad.moim.controller;


import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.moim.entity.Moim;
import backend.tangsquad.moim.dto.request.MoimCreateRequest;
import backend.tangsquad.moim.dto.request.MoimLeaderUpdateRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import backend.tangsquad.moim.dto.response.MoimCreateResponse;
import backend.tangsquad.moim.dto.response.MoimReadResponse;
import backend.tangsquad.moim.repository.MoimRepository;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.moim.service.MoimService;
import backend.tangsquad.common.service.UserService;
import backend.tangsquad.swagger.global.CommonResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    // Create a new moim
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

    @PostMapping("/register")
    @Operation(
            summary = "모임 등록",
            description = "사용자가 기존 모임에 등록합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<String> registerMoim(
            @RequestParam Long moimId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            Long userId = userDetails.getUser().getId();

            // Find the Moim by ID
            Optional<Moim> moimOptional = moimService.findById(moimId);
            if (moimOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Moim not found.");
            }

            Moim moim = moimOptional.get();

            // Check if the user is already registered
            if (moim.getRegisteredUsers().contains(userDetails.getUser())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already registered.");
            }

            // Register the user
            moim.getRegisteredUsers().add(userDetails.getUser());
            moimService.save(moim);

            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register user.");
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "모임 목록 조회",
            description = "모든 모임의 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimReadResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<MoimReadResponse>> getAllMoims() {

        try {
            List<Moim> moims = moimService.getAllMoims();

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
                            moim.getLocationThree(),
                            moim.getExpense(),
                            moim.getMoodOne(),
                            moim.getMoodTwo()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(moimReadResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/registered")
    @Operation(
            summary = "등록한 모임 목록 조회",
            description = "사용자가 등록한 모임 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록한 모임 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimReadResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록한 모임이 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<MoimReadResponse>> getRegisteredMoims(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            Long userId = userDetails.getUser().getId();
            List<Moim> moims = moimService.getRegisteredMoims(userId);

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
                            moim.getLocationThree(),
                            moim.getExpense(),
                            moim.getMoodOne(),
                            moim.getMoodTwo()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(moimReadResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("")
    @Operation(summary = "사용자가 생성한 모임 불러오기", description = "사용자가 생성한 모임들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
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
    @Operation(summary = "모임 수정하기", description = "사용자의 모임을 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<MoimReadResponse>> updateMoim(
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

    @PutMapping("/update-leader")
    @Operation(summary = "모임 소유자 수정하기", description = "모임의 소유자를 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<MoimReadResponse>> updateMoimLeader(
            @RequestBody MoimLeaderUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        Long moimId = request.getId();
        Long currentUserId = userDetailsImpl.getId();

        // Ensure the user is authorized to update this moim leader before attempting to update
        if (!moimService.isAuthorizedToUpdateLeader(moimId, currentUserId)) {
            throw new AccessDeniedException("User is not authorized to update this Moim");
        }

        // Call the service to update the Moim
        Moim updatedMoim = moimService.updateMoimLeader(moimId, request);

        // Convert the updated Moim to a MoimReadResponse
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

        // Return the updated MoimReadResponse in a list (to maintain consistency with previous GET mapping)
        return ResponseEntity.ok(Collections.singletonList(moimReadResponse));
    }



    @DeleteMapping("/{moimId}")
    @Operation(summary = "모임 삭제하기", description = "사용자의 모임을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<CommonResponse> deleteMoim(
            @PathVariable("moimId") Long moimId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        // Check if the user details are present
        if (userDetailsImpl == null) {
            // User is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the diving from the service
            Optional<Moim> moimOptional = moimService.getMoim(moimId);

            if (!moimOptional.isPresent()) {
                // Log not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Moim moim = moimOptional.get();
            Long moimOwnerId = moim.getUser().getId();

            System.out.println("userId: " + userDetailsImpl.getId() + "divingOwnerId: " + moimOwnerId);


            // Check if the authenticated user is authorized to delete the log
            if (!userDetailsImpl.getId().equals(moimOwnerId)) {
                // User is not authorized to delete this log
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Proceed to delete the diving
            moimService.deleteMoim(moimId);

            // Return success response
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // Handle other potential exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
