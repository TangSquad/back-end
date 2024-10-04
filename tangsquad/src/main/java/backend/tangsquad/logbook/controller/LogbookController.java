package backend.tangsquad.logbook.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogbookCreateRequest;
import backend.tangsquad.logbook.dto.response.LogbookReadRequest;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.dto.request.LogCreateRequest;
import backend.tangsquad.logbook.dto.request.LogUpdateRequest;
import backend.tangsquad.logbook.dto.response.LogCreateResponse;
import backend.tangsquad.logbook.dto.response.LogReadResponse;
import backend.tangsquad.logbook.service.LogbookService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// 로그북 내비게이션 바 - 내 다이빙 (나의 로그 CRUD)
@RequestMapping("/logbook")
@RestController
@Tag(name = "Logbook", description = "로그북 관련 API")
public class LogbookController {

    private final LogbookService logbookService;
    private final UserService userService;

    public LogbookController(LogbookService logbookService, UserService userService) {
        this.logbookService = logbookService;
        this.userService = userService;
    }

    // Create a new Logbook
    @PostMapping
    @Operation(
            summary = "로그북 생성",
            description = "새로운 로그북을 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "로그북 생성 성공", content = @Content(schema = @Schema(implementation = LogCreateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<LogbookCreateRequest> createLogbook(
            @RequestBody LogbookCreateRequest logbookCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("PostMapping called");

        Logbook savedLogbook = logbookService.save(logbookCreateRequest, userDetails);

        if (savedLogbook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logbookCreateRequest);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{logId}")
    @Operation(summary = "내 로그북 불러오기", description = "내 로그를 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookReadRequest> getMyLogbook(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("logId") Long logId) {
        // Fetch the logbook for the authenticated user
        Logbook logbook = logbookService.getLogbookByIdAndUserId(logId, userDetails.getId());

        if (logbook != null) {
            // Convert Logbook entity to LogbookReadRequest DTO
            LogbookReadRequest logbookReadRequest = convertToLogbookReadRequest(logbook);
            return ResponseEntity.ok(logbookReadRequest); // Return the logbook details
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Logbook not found
        }
    }

    private LogbookReadRequest convertToLogbookReadRequest(Logbook logbook) {
        return new LogbookReadRequest(
                logbook.getId(),
                logbook.getTitle(),
                logbook.getContents(),
                logbook.getDate(),
                logbook.getLocation(),
                logbook.getWeather(),
                logbook.getSurfTemp(),
                logbook.getUnderTemp()
        );
    }

    @GetMapping("")
    @Operation(summary = "내 로그북 불러오기", description = "나의 로그들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogbookReadRequest>> getMyLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = userDetails.getId();  // Use the authenticated user's ID

        try {
            // Retrieve the user's logs
            List<Logbook> logbooks = logbookService.getLogs(userId);

            // Map Logbook entities to LogbookReadRequest DTOs
            List<LogbookReadRequest> logbookReadRequests = logbooks.stream()
                    .map(logbook -> new LogbookReadRequest(
                            logbook.getId(),
                            logbook.getTitle(),
                            logbook.getContents(),
                            logbook.getDate(),
                            logbook.getLocation(),
                            logbook.getWeather(),
                            logbook.getSurfTemp(),
                            logbook.getUnderTemp()
                    ))
                    .collect(Collectors.toList());

            // Return the list of LogbookReadRequest
            return ResponseEntity.ok(logbookReadRequests);

        } catch (Exception e) {
            // Log the exception with details
            Logger logger = LoggerFactory.getLogger(LogbookController.class);
            logger.error("Error retrieving logs for user ID: " + userId, e);

            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Return an empty list on error, adjust response as needed
        }
    }




    @GetMapping("/user/{userId}")
    @Operation(summary = "유저 로그북 불러오기", description = "해당 유저의 로그들을 불러옵니다.")
    public ResponseEntity<List<LogReadResponse>> getLogs(@PathVariable("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("userId: " + userId);
//         Retrieve the user's logs
        List<Logbook> logbooks = logbookService.getLogs(userId);


        // Map Logbook entities to LogReadResponse DTOs
        List<LogReadResponse> logReadResponses = logbooks.stream()
                .map(logbook -> new LogReadResponse(
//                        logbook.getId(),
//                        logbook.getUser(),
//                        logbook.getDate(),
//                        logbook.getTitle(),
//                        logbook.getSquadId(),
//                        logbook.getContents(),
//                        logbook.getLocation(),
//                        logbook.getWeather(),
//                        logbook.getSurfTemp(),
//                        logbook.getUnderTemp()
                ))
                .collect(Collectors.toList());

        // Return the list of LogReadResponse
        return ResponseEntity.ok(logReadResponses);
    }


    @GetMapping("/user/{userId}/{logId}")
    @Operation(summary = "타 유저 로그북 불러오기", description = "해당 유저의 로그를 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogReadResponse> getUserLog(
            @PathVariable("userId") Long userId,
            @PathVariable("logId") Long logId) {


        // Retrieve the logbook entry by ID
        Optional<Logbook> logbookOptional = logbookService.getLog(logId);

        if (logbookOptional.isPresent()) {
            Logbook logbook = logbookOptional.get();

            // Check if the logbook belongs to the requested user
            if (logbook.getUser().getId().equals(userId)) {
                // Map Logbook entity to LogReadResponse DTO
                LogReadResponse logReadResponse = new LogReadResponse(
//                        logbook.getId(),
//                        logbook.getUser(),  // Assuming you want to include the user ID
//                        logbook.getDate(),
//                        logbook.getTitle(),
//                        logbook.getSquadId(),
//                        logbook.getContents(),
//                        logbook.getLocation(),
//                        logbook.getWeather(),
//                        logbook.getSurfTemp(),
//                        logbook.getUnderTemp()
                );

                return ResponseEntity.ok(logReadResponse);
            } else {
                // Return forbidden status if the logbook does not belong to the requested user
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        // Return not found status if the logbook entry doesn't exist
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }




    // Use @PathVariable for the ID since it's in the URL path
    // 수정중
    @PutMapping("")
    @Operation(summary = "로그북 수정하기", description = "나의 로그를 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogReadResponse>> updateLog(
            @RequestBody LogUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        // Extract the logId from the request (assuming LogUpdateRequest contains logId)
        Long logId = request.getId();

        // Call the service to update the logbook
        Optional<Logbook> updatedLogbookOptional = Optional.ofNullable(logbookService.updateLog(logId, request));
        // If the logbook was updated successfully
        if (updatedLogbookOptional.isPresent()) {
            Logbook updatedLogbook = updatedLogbookOptional.get();

            // Check if the user IDs match
            if (updatedLogbook.getUser() == null) {
                throw new IllegalStateException("User associated with logbook is null");
            }
            if (!userDetailsImpl.getId().equals(updatedLogbook.getUser().getId())) {
                throw new AccessDeniedException("User is not authorized to update this logbook");
            }

            // Convert the updated logbook to a LogReadResponse
            LogReadResponse logReadResponse = new LogReadResponse(
//                    updatedLogbook.getId(),
//                    updatedLogbook.getUser(),
//                    updatedLogbook.getDate(),
//                    updatedLogbook.getTitle(),
//                    updatedLogbook.getSquadId(),
//                    updatedLogbook.getContents(),
//                    updatedLogbook.getLocation(),
//                    updatedLogbook.getWeather(),
//                    updatedLogbook.getSurfTemp(),
//                    updatedLogbook.getUnderTemp()
//                    updatedLogbook.getViewSight(),
//                    updatedLogbook.getTide(),
//                    updatedLogbook.getStartDiveTime(),
//                    updatedLogbook.getEndDiveTime(),
//                    updatedLogbook.getTimeDiffDive(),
//                    updatedLogbook.getAvgDepDiff(),
//                    updatedLogbook.getMaxDiff(),
//                    updatedLogbook.getStartBar(),
//                    updatedLogbook.getEndBar(),
//                    updatedLogbook.getDiffBar()
            );
            // Return the updated LogReadResponse in a list (to maintain consistency with previous GET mapping)
            return ResponseEntity.ok(Collections.singletonList(logReadResponse));
        } else {
            // Return a NOT_FOUND status if the logbook could not be found or updated
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }





    // Use @PathVariable for the ID since it's in the URL path
    @DeleteMapping("/{logId}")
    @Operation(summary = "로그북 삭제하기", description = "나의 로그를 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<CommonResponse> deleteLog(
            @PathVariable("logId") Long logId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        // Check if the user details are present
        if (userDetailsImpl == null) {
            // User is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the logbook from the service
            Optional<Logbook> logbookOptional = logbookService.getLog(logId);

            if (!logbookOptional.isPresent()) {
                // Log not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Logbook logbook = logbookOptional.get();
            Long logOwnerId = logbook.getUser().getId();

            System.out.println("userId: " + userDetailsImpl.getId() + "logOwnerId: " + logOwnerId);


            // Check if the authenticated user is authorized to delete the log
            if (!userDetailsImpl.getId().equals(logOwnerId)) {
                // User is not authorized to delete this log
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Proceed to delete the logbook
            logbookService.deleteLog(logId);

            // Return success response
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // Handle other potential exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
