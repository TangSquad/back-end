package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogCreateRequest;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.response.LogCreateResponse;
import backend.tangsquad.dto.response.LogReadResponse;
import backend.tangsquad.service.LogbookService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private UserService userService;

    @Autowired
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
    public ResponseEntity<LogCreateRequest> createLogbook(
            @RequestBody LogCreateRequest logCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("PostMapping called");
        try {
            Logbook logbook = new Logbook();
            User user = new User();
//            user.setId(userDetails.getId());
            // 테스트용
            user.setId(logCreateRequest.getUserId());
            logbook.setUser(user);

            logbook.setTitle(logCreateRequest.getTitle());
            logbook.setDate(logCreateRequest.getDate());
            logbook.setContents(logCreateRequest.getContents());
            logbook.setLocation(logCreateRequest.getLocation());
            logbook.setWeather(logCreateRequest.getWeather());
            logbook.setSurfTemp(logCreateRequest.getSurfTemp());
            logbook.setUnderTemp(logCreateRequest.getUnderTemp());
            logbook.setViewSight(logCreateRequest.getViewSight());
            logbook.setTide(logCreateRequest.getTide());
            logbook.setStartDiveTime(logCreateRequest.getStartDiveTime());
            logbook.setEndDiveTime(logCreateRequest.getEndDiveTime());
            logbook.setTimeDiffDive(logCreateRequest.getTimeDiffDive());
            logbook.setAvgDepDiff(logCreateRequest.getAvgDepDiff());
            logbook.setMaxDiff(logCreateRequest.getMaxDiff());
            logbook.setStartBar(logCreateRequest.getStartBar());
            logbook.setEndBar(logbook.getEndBar());
            logbook.setDiffBar(logbook.getDiffBar());
            // Save logbook
            Logbook savedLogbook = logbookService.save(logbook);
            return ResponseEntity.status(HttpStatus.CREATED).body(logCreateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WRONG");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/{logId}")
    @Operation(summary = "내 로그북 불러오기", description = "내 로그를 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogReadResponse> getMyLog(@PathVariable("logId") Long logId) {
        Logger logger = LoggerFactory.getLogger(LogbookController.class);

        logger.debug("GetMapping called, log ID: {}", logId);
        try {
            Optional<Logbook> logbookOptional = logbookService.getLog(logId);
            if (logbookOptional.isPresent()) {
                Logbook logbook = logbookOptional.get();
                logger.info("Logbook found: ID = {}, Title = {}", logbook.getId(), logbook.getTitle());

                // Convert Logbook to LogReadResponse
                LogReadResponse logReadResponse = new LogReadResponse(
                        logbook.getId(),
                        logbook.getUser().getId(),
                        logbook.getDate(),
                        logbook.getTitle(),
                        logbook.getSquadId(),
                        logbook.getContents(),
                        logbook.getLocation(),
                        logbook.getWeather(),
                        logbook.getSurfTemp(),
                        logbook.getUnderTemp(),
                        logbook.getViewSight(),
                        logbook.getTide(),
                        logbook.getStartDiveTime(),
                        logbook.getEndDiveTime(),
                        logbook.getTimeDiffDive(),
                        logbook.getAvgDepDiff(),
                        logbook.getMaxDiff(),
                        logbook.getStartBar(),
                        logbook.getEndBar(),
                        logbook.getDiffBar()
                );

                // Return the response entity with the log data
                return ResponseEntity.ok(logReadResponse);
            } else {
                logger.warn("Logbook not found for ID: {}", logId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving logbook with ID: " + logId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("")
    @Operation(summary = "내 로그북 불러오기", description = "나의 로그들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogReadResponse>> getMyLogs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = 1L;
//        Long userId = userDetails.getId();  // Use the authenticated user's ID

        try {
            System.out.println("userId: " + userId);

            // Retrieve the user's logs
            List<Logbook> logbooks = logbookService.getLogs(userId);

            // Map Logbook entities to LogReadResponse DTOs
            List<LogReadResponse> logReadResponses = logbooks.stream()
                    .map(logbook -> new LogReadResponse(
                            logbook.getId(),
                            1L,
//                            logbook.getUser().getId(),  // Retrieve the actual user ID
                            logbook.getDate(),
                            logbook.getTitle(),
                            logbook.getSquadId(),
                            logbook.getContents(),
                            logbook.getLocation(),
                            logbook.getWeather(),
                            logbook.getSurfTemp(),
                            logbook.getUnderTemp(),
                            logbook.getViewSight(),
                            logbook.getTide(),
                            logbook.getStartDiveTime(),
                            logbook.getEndDiveTime(),
                            logbook.getTimeDiffDive(),
                            logbook.getAvgDepDiff(),
                            logbook.getMaxDiff(),
                            logbook.getStartBar(),
                            logbook.getEndBar(),
                            logbook.getDiffBar()
                    ))
                    .collect(Collectors.toList());

            // Return the list of LogReadResponse
            return ResponseEntity.ok(logReadResponses);

        } catch (Exception e) {
            // Log the exception with details
            Logger logger = LoggerFactory.getLogger(LogbookController.class);
            logger.error("Error retrieving logs for user ID: " + userId, e);

            // Return a generic error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new LogReadResponse()));  // Adjust response as needed
        }
    }



    @GetMapping("/user/{userId}")
    @Operation(summary = "유저 로그북 불러오기", description = "해당 유저의 로그들을 불러옵니다.")
    public ResponseEntity<List<LogReadResponse>> getLogs(@PathVariable("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Retrieve the current authenticated user's ID

//        Long userId = userDetails.getId();
        System.out.println("userId: " + userId);
//         Retrieve the user's logs
        List<Logbook> logbooks = logbookService.getLogs(userId);
//        List<Logbook> logbooks = logbookService.getLogs(1L);


        // Map Logbook entities to LogReadResponse DTOs
        List<LogReadResponse> logReadResponses = logbooks.stream()
                .map(logbook -> new LogReadResponse(
                        logbook.getId(),
//                        1L,
                        logbook.getUser().getId(),
                        logbook.getDate(),
                        logbook.getTitle(),
                        logbook.getSquadId(),
                        logbook.getContents(),
                        logbook.getLocation(),
                        logbook.getWeather(),
                        logbook.getSurfTemp(),
                        logbook.getUnderTemp(),
                        logbook.getViewSight(),
                        logbook.getTide(),
                        logbook.getStartDiveTime(),
                        logbook.getEndDiveTime(),
                        logbook.getTimeDiffDive(),
                        logbook.getAvgDepDiff(),
                        logbook.getMaxDiff(),
                        logbook.getStartBar(),
                        logbook.getEndBar(),
                        logbook.getDiffBar()
                ))
                .collect(Collectors.toList());

        // Return the list of LogReadResponse
        return ResponseEntity.ok(logReadResponses);
    }


    @GetMapping("/user/{userId}/{logId}")
    @Operation(summary = "타 유저 로그북 불러오기", description = "해당 유저의 로그를 불러옵니다.")
    public ResponseEntity<LogReadResponse> getUserLog(
            @PathVariable("userId") Long userId,
            @PathVariable("logId") Long logId) {

        // Extract the ID of the currently authenticated user
//        Long currentUserId = userDetails.getId();  // Ensure this method exists in UserDetailsImpl

        // Retrieve the logbook entry by ID
        Optional<Logbook> logbookOptional = logbookService.getLog(logId);

        if (logbookOptional.isPresent()) {
            Logbook logbook = logbookOptional.get();

            // Check if the logbook belongs to the requested user
            if (logbook.getUser().getId().equals(userId)) {
                // Map Logbook entity to LogReadResponse DTO
                LogReadResponse logReadResponse = new LogReadResponse(
                        logbook.getId(),
                        logbook.getUser().getId(),  // Assuming you want to include the user ID
                        logbook.getDate(),
                        logbook.getTitle(),
                        logbook.getSquadId(),
                        logbook.getContents(),
                        logbook.getLocation(),
                        logbook.getWeather(),
                        logbook.getSurfTemp(),
                        logbook.getUnderTemp(),
                        logbook.getViewSight(),
                        logbook.getTide(),
                        logbook.getStartDiveTime(),
                        logbook.getEndDiveTime(),
                        logbook.getTimeDiffDive(),
                        logbook.getAvgDepDiff(),
                        logbook.getMaxDiff(),
                        logbook.getStartBar(),
                        logbook.getEndBar(),
                        logbook.getDiffBar()
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
//            if (!userDetailsImpl.getId().equals(updatedLogbook.getUser().getId())) {
//                throw new AccessDeniedException("User is not authorized to update this logbook");
//            }

            // Convert the updated logbook to a LogReadResponse
            LogReadResponse logReadResponse = new LogReadResponse(
                    updatedLogbook.getId(),
                    updatedLogbook.getUser().getId(),
                    updatedLogbook.getDate(),
                    updatedLogbook.getTitle(),
                    updatedLogbook.getSquadId(),
                    updatedLogbook.getContents(),
                    updatedLogbook.getLocation(),
                    updatedLogbook.getWeather(),
                    updatedLogbook.getSurfTemp(),
                    updatedLogbook.getUnderTemp(),
                    updatedLogbook.getViewSight(),
                    updatedLogbook.getTide(),
                    updatedLogbook.getStartDiveTime(),
                    updatedLogbook.getEndDiveTime(),
                    updatedLogbook.getTimeDiffDive(),
                    updatedLogbook.getAvgDepDiff(),
                    updatedLogbook.getMaxDiff(),
                    updatedLogbook.getStartBar(),
                    updatedLogbook.getEndBar(),
                    updatedLogbook.getDiffBar()
            );
            // Return the updated LogReadResponse in a list (to maintain consistency with previous GET mapping)
            return ResponseEntity.ok(Collections.singletonList(logReadResponse));
        } else {
            // Return a NOT_FOUND status if the logbook could not be found or updated
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }





    // Use @PathVariable for the ID since it's in the URL path
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteLog(
            @PathVariable("id") Long logId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        try {
            // Retrieve the logbook from the service
            Optional<Logbook> logbookOptional = logbookService.getLog(logId);

            if (!logbookOptional.isPresent()) {
                // Log not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build();
            }

            Logbook logbook = logbookOptional.get();
            Long logOwnerId = logbook.getUser().getId();

//            // Check if the authenticated user is authorized to delete the log
//            if (!userDetailsImpl.getId().equals(logOwnerId)) {
//                // User is not authorized to delete this log
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .build();
//            }

            // Proceed to delete the logbook
            logbookService.deleteLog(logId);

            // Return success response
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            // Handle other potential exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}