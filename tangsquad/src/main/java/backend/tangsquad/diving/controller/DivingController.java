package backend.tangsquad.diving.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.dto.request.DivingCreateRequest;
import backend.tangsquad.diving.dto.request.DivingUpdateRequest;
import backend.tangsquad.diving.dto.response.DivingCreateResponse;
import backend.tangsquad.diving.dto.response.DivingReadResponse;
import backend.tangsquad.diving.service.DivingService;
import backend.tangsquad.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/diving")
@RestController
@Tag(name = "Diving", description = "다이빙 관련 API")
public class DivingController {

    private final DivingService divingService;
    private final UserService userService; // Use UserService to handle user-related operations

    @Autowired
    public DivingController(DivingService divingService, UserService userService) {
        this.divingService = divingService;
        this.userService = userService;
    }

    // Create a new diving
    @PostMapping
    @Operation(
            summary = "다이빙 생성",
            description = "새로운 다이빙을 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "다이빙 생성 성공", content = @Content(schema = @Schema(implementation = DivingCreateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<DivingCreateRequest> createDiving(
            @RequestBody DivingCreateRequest divingCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            Diving diving = new Diving();

            diving.setUser(userDetails.getUser());
            diving.setDivingName(divingCreateRequest.getDivingName());
            diving.setDivingIntro(divingCreateRequest.getDivingIntro());
            diving.setAge(divingCreateRequest.getAge());
            diving.setLocation(divingCreateRequest.getLocation());
            diving.setLimitLicense(divingCreateRequest.getLimitLicense());
            diving.setLimitPeople(divingCreateRequest.getLimitPeople());
            diving.setMoodOne(divingCreateRequest.getMoodOne());
            diving.setMoodTwo(divingCreateRequest.getMoodTwo());
            diving.setStartDate(divingCreateRequest.getStartDate());
            diving.setEndDate(divingCreateRequest.getEndDate());

            // Save diving
            Diving savedDiving = divingService.save(diving);
            return ResponseEntity.status(HttpStatus.CREATED).body(divingCreateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WRONG");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("")
    @Operation(summary = "내 다이빙 불러오기", description = "나의 다이빙들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<DivingReadResponse>> getMyDivings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = userDetails.getId();  // Use the authenticated user's ID

        try {
            // Retrieve the user's logs
            List<Diving> divings = divingService.getDivings(userId);

            // Map Diving entities to DivingReadResponse DTOs
            List<DivingReadResponse> divingReadResponses = divings.stream()
                    .map(diving -> new DivingReadResponse(
                            diving.getDivingId(),
                            diving.getUser().getId(),  // Corrected to retrieve the actual user ID
                            diving.getDivingName(),
                            diving.getDivingIntro(),
                            diving.getAge(),
                            diving.getMoodOne(),
                            diving.getMoodTwo(),
                            diving.getLimitPeople(),
                            diving.getLimitLicense(),
                            diving.getStartDate(),
                            diving.getEndDate(),
                            diving.getLocation()
                    ))
                    .collect(Collectors.toList());

            // Return the list of DivingReadResponse
            return ResponseEntity.ok(divingReadResponses);

        } catch (Exception e) {
            // Log the exception with details
            Logger logger = LoggerFactory.getLogger(DivingController.class);
            logger.error("Error retrieving logs for user ID: " + userId, e);

            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new DivingReadResponse()));  // Adjust response as needed
        }
    }

    @GetMapping("/{divingId}")
    @Operation(summary = "다이빙 불러오기", description = "특정 다이빙을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<DivingReadResponse> getMyDiving(@PathVariable("divingId") Long divingId) {
        Logger logger = LoggerFactory.getLogger(DivingController.class);

        logger.debug("GetMapping called, log ID: {}", divingId);
        try {
            Optional<Diving> divingOptional = divingService.getDiving(divingId);
            if (divingOptional.isPresent()) {
                Diving diving = divingOptional.get();
                logger.info("diving found: ID = {}, Title = {}", diving.getDivingId(), diving.getDivingName());

                // Convert diving to LogReadResponse
                DivingReadResponse divingReadResponse = new DivingReadResponse(
                        diving.getDivingId(),
                        diving.getUser().getId(),  // Corrected to retrieve the actual user ID
                        diving.getDivingName(),
                        diving.getDivingIntro(),
                        diving.getAge(),
                        diving.getMoodOne(),
                        diving.getMoodTwo(),
                        diving.getLimitPeople(),
                        diving.getLimitLicense(),
                        diving.getStartDate(),
                        diving.getEndDate(),
                        diving.getLocation()
                );

                // Return the response entity with the log data
                return ResponseEntity.ok(divingReadResponse);
            } else {
                logger.warn("diving not found for ID: {}", divingId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving diving with ID: " + divingId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PutMapping("")
    @Operation(summary = "다이빙 수정하기", description = "나의 다이빙 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<DivingReadResponse>> updateDiving(
            @RequestBody DivingUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        // Extract the logId from the request (assuming LogUpdateRequest contains logId)
        Long divingId = request.getDivingId();

        // Call the service to update the diving
        Optional<Diving> updatedDivingOptional = Optional.ofNullable(divingService.updateDiving(divingId, request));
        // If the diving was updated successfully
        if (updatedDivingOptional.isPresent()) {
            Diving updatedDiving = updatedDivingOptional.get();

            // Check if the user IDs match
            if (updatedDiving.getUser() == null) {
                throw new IllegalStateException("User associated with diving is null");
            }
            if (!userDetailsImpl.getId().equals(updatedDiving.getUser().getId())) {
                throw new AccessDeniedException("User is not authorized to update this ");
            }

            // Convert the updated diving to a LogReadResponse
            DivingReadResponse divingReadResponse = new DivingReadResponse(
                    updatedDiving.getDivingId(),
                    updatedDiving.getUser().getId(),  // Corrected to retrieve the actual user ID
                    updatedDiving.getDivingName(),
                    updatedDiving.getDivingIntro(),
                    updatedDiving.getAge(),
                    updatedDiving.getMoodOne(),
                    updatedDiving.getMoodTwo(),
                    updatedDiving.getLimitPeople(),
                    updatedDiving.getLimitLicense(),
                    updatedDiving.getStartDate(),
                    updatedDiving.getEndDate(),
                    updatedDiving.getLocation()
            );
            // Return the updated LogReadResponse in a list (to maintain consistency with previous GET mapping)
            return ResponseEntity.ok(Collections.singletonList(divingReadResponse));
        } else {
            // Return a NOT_FOUND status if the diving could not be found or updated
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/{divingId}")
    @Operation(summary = "다이빙 삭제하기", description = "나의 다이빙을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<CommonResponse> deleteLog(
            @PathVariable("divingId") Long divingId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        // Check if the user details are present
        if (userDetailsImpl == null) {
            // User is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the diving from the service
            Optional<Diving> divingOptional = divingService.getDiving(divingId);

            if (!divingOptional.isPresent()) {
                // Log not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Diving diving = divingOptional.get();
            Long divingOwnerId = diving.getUser().getId();

            System.out.println("userId: " + userDetailsImpl.getId() + "divingOwnerId: " + divingOwnerId);


            // Check if the authenticated user is authorized to delete the log
            if (!userDetailsImpl.getId().equals(divingOwnerId)) {
                // User is not authorized to delete this log
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Proceed to delete the diving
            divingService.deleteDiving(divingId);

            // Return success response
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // Handle other potential exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
