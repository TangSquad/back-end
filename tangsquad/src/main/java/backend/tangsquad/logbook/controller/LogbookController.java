package backend.tangsquad.logbook.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.like.dto.request.LikeLogbookRequest;
import backend.tangsquad.like.service.LikeLogbookService;
import backend.tangsquad.logbook.dto.request.LogbookCreateRequest;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.service.LogbookService;
import backend.tangsquad.swagger.global.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// 로그북 내비게이션 바 - 내 다이빙 (나의 로그 CRUD)
@RequestMapping("/logbook")
@RestController
@RequiredArgsConstructor
@Tag(name = "Logbook", description = "로그북 관련 API")
public class LogbookController {

    private final LogbookService logbookService;
    private final LikeLogbookService likeLogbookService;

    @PostMapping
    @Operation(
            summary = "로그북 생성",
            description = "새로운 로그북을 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<LogbookCreateRequest> createLogbook(
            @RequestBody LogbookCreateRequest logbookCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        LogbookResponse logbookResponse = logbookService.save(logbookCreateRequest, userDetails);

        if (logbookResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logbookCreateRequest);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{logId}")
    @Operation(summary = "내 로그북 불러오기", description = "내 로그를 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> getMyLogbook(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("logId") Long logId) {
        Logbook logbook = logbookService.getLogbookByIdAndUserId(logId, userDetails.getId());

<<<<<<< HEAD
        if (logbook != null) {
            LogbookResponse logbookResponse = convertToLogbookResponse(logbook);
            return ResponseEntity.ok(logbookResponse); // Return the logbook details
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Logbook not found
=======
        logger.debug("GetMapping called, log ID: {}", logId);
        try {
            Optional<Logbook> logbookOptional = logbookService.getLog(logId);
            if (logbookOptional.isPresent()) {
                Logbook logbook = logbookOptional.get();
                logger.info("Logbook found: ID = {}, Title = {}", logbook.getId(), logbook.getTitle());

                // Convert Logbook to LogReadResponse
                LogReadResponse logReadResponse = new LogReadResponse(
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
>>>>>>> 2eb619e (chore: 테스트를 위하여 기존의 코드 주석 처리)
        }
    }

    @GetMapping("/user/{logId}")
    @Operation(summary = "유저 로그북 불러오기", description = "해당 유저의 로그를 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> getUserLog(
            @PathVariable("logId") Long logId) {

        // Retrieve the logbook entry by ID
        Logbook logbook = logbookService.getLogbookByLogbookId(logId);

        if (logbook != null) {
            // Convert Logbook entity to LogbookReadRequest DTO
            LogbookResponse logbookResponse = convertToLogbookResponse(logbook);
            return ResponseEntity.ok(logbookResponse); // Return the logbook details
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Logbook not found
        }
    }

    private LogbookResponse convertToLogbookResponse(Logbook logbook) {
        return new LogbookResponse(
                logbook.getId(),
                logbook.getIsPublic(),
                logbook.getUser().getId(),
                logbook.getTitle(),
                logbook.getContents(),
                logbook.getDate(),
                logbook.getLocation()
        );
    }

    @GetMapping("")
    @Operation(summary = "내 로그북 불러오기", description = "나의 로그들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogbookRequest>> getMyLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LogbookRequest> logbookRequests = logbookService.getLogbooksByUserId(userDetails.getUser().getId());

<<<<<<< HEAD
        if (logbookRequests != null) {
            return ResponseEntity.ok(logbookRequests);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
=======
        try {

            // Retrieve the user's logs
            List<Logbook> logbooks = logbookService.getLogs(userId);

            // Map Logbook entities to LogReadResponse DTOs
            List<LogReadResponse> logReadResponses = logbooks.stream()
                    .map(logbook -> new LogReadResponse(
//                            logbook.getId(),
//                            logbook.getUser(),  // Retrieve the actual user ID
//                            logbook.getDate(),
//                            logbook.getTitle(),
//                            logbook.getSquadId(),
//                            logbook.getContents(),
//                            logbook.getLocation(),
//                            logbook.getWeather(),
//                            logbook.getSurfTemp(),
//                            logbook.getUnderTemp()
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
>>>>>>> 2eb619e (chore: 테스트를 위하여 기존의 코드 주석 처리)
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "유저 로그북 불러오기", description = "해당 유저의 로그들을 불러옵니다.")
    public ResponseEntity<List<LogbookRequest>> getLogbooks(@PathVariable("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LogbookRequest> logbookRequests = logbookService.getLogbooksByUserId(userId);

<<<<<<< HEAD
        if (logbookRequests != null) {
            return ResponseEntity.ok(logbookRequests);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
=======
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
>>>>>>> 2eb619e (chore: 테스트를 위하여 기존의 코드 주석 처리)
        }
    }

    @PutMapping("")
    @Operation(summary = "로그북 수정하기", description = "나의 로그를 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> updateLog(
            @RequestBody LogbookRequest logbookRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        LogbookResponse logbookResponse = logbookService.updateLog(logbookRequest, userDetailsImpl);

<<<<<<< HEAD
        if (logbookResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
=======
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
>>>>>>> 2eb619e (chore: 테스트를 위하여 기존의 코드 주석 처리)
        } else {
            return ResponseEntity.ok(logbookResponse);
        }
    }

    // Use @PathVariable for the ID since it's in the URL path
    @DeleteMapping("/{logId}")
    @Operation(summary = "로그북 삭제하기", description = "나의 로그를 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<CommonResponse> deleteLog(
            @PathVariable("logId") Long logId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return logbookService.deleteLog(logId, userDetails);
    }


    // 로그북 인스턴스 모두 나와야 함.
    @PostMapping("like/{logbookId}")
    @Operation(summary = "좋아요 로그북 추가", description = "로그북에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LikeLogbookRequest> likeLogbook(@PathVariable Long logbookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LikeLogbookRequest likeLogbookRequest = likeLogbookService.createLike(logbookId, userDetails);
        return ResponseEntity.ok(likeLogbookRequest);
    }

    @GetMapping("like")
    @Operation(summary = "좋아요한 로그북 가져오기", description = "좋아요한 로그북을 가져옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogbookResponse>> getLikeLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LogbookResponse> logbookResponses = likeLogbookService.getLikeLogbooks(userDetails);

        if (logbookResponses != null) {
            return ResponseEntity.ok(logbookResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("like/{logId}")
    @Operation(summary = "좋아요한 로그북 취소하기", description = "좋아요한 로그북을 취소합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> cancelLikeLogbooks(@PathVariable("logId") Long logId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LogbookResponse logbookResponse = likeLogbookService.cancelLike(logId, userDetails);
        if (logbookResponse != null) {
            return ResponseEntity.ok(logbookResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}