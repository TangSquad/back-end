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

        if (logbook != null) {
            LogbookResponse logbookResponse = convertToLogbookResponse(logbook);
            return ResponseEntity.ok(logbookResponse); // Return the logbook details
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Logbook not found
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

        if (logbookRequests != null) {
            return ResponseEntity.ok(logbookRequests);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "유저 로그북 불러오기", description = "해당 유저의 로그들을 불러옵니다.")
    public ResponseEntity<List<LogbookRequest>> getLogbooks(@PathVariable("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LogbookRequest> logbookRequests = logbookService.getLogbooksByUserId(userId);

        if (logbookRequests != null) {
            return ResponseEntity.ok(logbookRequests);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("")
    @Operation(summary = "로그북 수정하기", description = "나의 로그를 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> updateLog(
            @RequestBody LogbookRequest logbookRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        LogbookResponse logbookResponse = logbookService.updateLog(logbookRequest, userDetailsImpl);

        if (logbookResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
}
