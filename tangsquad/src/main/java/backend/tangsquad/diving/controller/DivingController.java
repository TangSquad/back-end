package backend.tangsquad.diving.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.service.DivingService;
import backend.tangsquad.like.dto.request.LikeDivingRequest;
import backend.tangsquad.like.service.LikeDivingService;
import backend.tangsquad.swagger.global.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/diving")
@RestController
@RequiredArgsConstructor
@Tag(name = "Diving", description = "다이빙 관련 API")
public class DivingController {

    private final DivingService divingService;
    private final LikeDivingService likeDivingService;

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
    public ResponseEntity<DivingResponse> createDiving(
            @RequestBody DivingRequest divingRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            Diving diving = Diving.builder()
                    .user(userDetails.getUser())
                    .divingName(divingRequest.getDivingName())
                    .divingIntro(divingRequest.getDivingIntro())
                    .age(divingRequest.getAge())
                    .moods(divingRequest.getMoods())
                    .limitPeople(divingRequest.getLimitPeople())
                    .limitLicense(divingRequest.getLimitLicense())
                    .startDate(divingRequest.getStartDate())
                    .endDate(divingRequest.getEndDate())
                    .location(divingRequest.getLocation())
                    .build();

            // Save diving
            Diving savedDiving = divingService.save(diving);
            return ResponseEntity.status(HttpStatus.CREATED).body(divingRequest);
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
    public ResponseEntity<List<DivingResponse>> updateDiving(
            @RequestBody DivingRequest divingRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

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

    @PostMapping("like/{divingId}")
    @Operation(summary = "좋아요 다이빙 추가", description = "다이빙에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LikeDivingRequest> likeDiving(@PathVariable Long divingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LikeDivingRequest likeDivingRequest = likeDivingService.createLike(divingId, userDetails);
        return ResponseEntity.ok(likeDivingRequest);
    }

    @GetMapping("like")
    @Operation(summary = "좋아요한 다이빙 가져오기", description = "좋아요한 로그북을 가져옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<DivingRequest>> getLikeDivings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<DivingRequest> divingRequests = likeDivingService.getLikeDivings(userDetails);

        if (divingRequests != null) {
            return ResponseEntity.ok(divingRequests);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
