package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogCreateRequest;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.response.LogCreateResponse;
import backend.tangsquad.dto.response.LogReadResponse;
import backend.tangsquad.repository.LogbookRepository;
import backend.tangsquad.repository.UserRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


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
    public ResponseEntity<Logbook> createLogbook(
            @RequestBody Logbook logbook,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("PostMapping called");
        try {
            // Log the incoming request body
            System.out.println("Received Logbook: " + logbook);

            // Set default values for missing fields (for testing purposes)
            if (logbook.getDate() == null) {
                logbook.setDate(LocalDateTime.now());
            }
            if (logbook.getUser() == null) {
                logbook.setUser(userDetails.getUser());
            }

            // Validate fields
            if (logbook.getDate() == null || logbook.getTitle() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            // Save logbook
            Logbook savedLogbook = logbookService.save(logbook);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLogbook);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WRONG");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{logId}")
    @Operation(summary = "내 로그북 불러오기", description = "내 로그들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
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





    @GetMapping("/")
    public List<Logbook> getMyLogs(@AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the current authenticated user's ID
        Long userId = ((UserDetailsImpl) userDetails).getId();

        // Get the user from the service layer
        User user = userService.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retrieve the user's logs
        return logbookService.getLogs(user);
    }


//    @GetMapping("/{username}")
//    public List<Logbook> getLogs(@PathVariable("username") String username,
//                                 @AuthenticationPrincipal UserDetails userDetails) {
//        // Retrieve the current authenticated user's ID
//        Long userId = ((UserDetailsImpl) userDetails).getId();
//
//        // Get the user from the service
//        User user = userService.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Fetch the logs for the user
//        return logbookService.getLogs(user);
//    }
//    // Use @PathVariable for the ID since it's in the URL path


    @GetMapping("/{username}/{logId}")
    public ResponseEntity<Logbook> getLog(
            @PathVariable("username") String username,
            @PathVariable("logId") Long logId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Extract the username from the authenticated user's details
        String currentUsername = userDetails.getUsername();

        // Retrieve the logbook entry by ID
        Optional<Logbook> logbookOptional = logbookService.getLog(logId);

        if (logbookOptional.isPresent()) {
            Logbook logbook = logbookOptional.get();

            // Check if the logbook belongs to the requested user
            if (logbook.getUser().getUsername().equals(username)) {
                // Implement permission check logic if needed
                // For instance, if you allow access based on roles or specific permissions

                return ResponseEntity.ok(logbook);
            } else {
                // Return a forbidden status if the logbook does not belong to the requested user
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        // Return not found status if the logbook entry doesn't exist
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    // Use @PathVariable for the ID since it's in the URL path
    // 수정중
    @PutMapping("/{id}")
    public Logbook updateLog (
            @PathVariable("id") Long logId,
            @RequestBody LogUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        Long userId = userDetailsImpl.getId();
//        Logbook logbook = logbookService.updateLog(userId, logId, request);
//        return logbook;
        return new Logbook();
    }

    // Use @PathVariable for the ID since it's in the URL path
    @DeleteMapping("/{id}")
    public CommonResponse<LogReadResponse> deleteLog(
            @PathVariable("id") Long logId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        Long userId = userDetailsImpl.getId();
//        logbookService.deleteLog(userId, logId);
        return CommonResponse.success();
    }
}
