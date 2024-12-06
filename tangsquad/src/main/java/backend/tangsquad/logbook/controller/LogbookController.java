package backend.tangsquad.logbook.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
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

import static backend.tangsquad.converter.ConvertTo.convertToLogbookResponse;

@RequestMapping("/logbook")
@RestController
@RequiredArgsConstructor
@Tag(name = "Logbook", description = "로그북 관련 API")
public class LogbookController {

    private final LogbookService logbookService;
    private final LikeLogbookService likeLogbookService;

    private ResponseEntity<LogbookResponse> checkUserDetailsAndRespond(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        return null;
    }

    @PostMapping
    @Operation(
            summary = "로그북 생성",
            description = "새로운 로그북을 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<LogbookResponse> createLogbook(
            @RequestBody LogbookCreateRequest logbookCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return response;

        LogbookResponse logbookResponse = logbookService.save(logbookCreateRequest, userDetails);

        if (logbookResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logbookResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("{logbookId}")
    @Operation(summary = "내 로그북 불러오기", description = "내 로그를 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> getMyLogbook(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("logbookId") Long logbookId) {
        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return response;

        Logbook logbook = logbookService.getLogbookByIdAndUserId(logbookId, userDetails.getId());

        if (logbook != null) {
            LogbookResponse logbookResponse = convertToLogbookResponse(logbook, userDetails.getUser().getUserProfile());
            return ResponseEntity.ok(logbookResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("all")
    @Operation(summary = "모든 로그북 불러오기", description = "모든 로그북을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogbookResponse>> getAllLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        List<LogbookResponse> logbookResponses = logbookService.getAllLogbooks(userDetails);

        if (logbookResponses != null && !logbookResponses.isEmpty()) {
            return ResponseEntity.ok(logbookResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("")
    @Operation(summary = "내 로그북 불러오기", description = "나의 로그들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogbookResponse>> getMyLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        List<LogbookResponse> logbookResponses = logbookService.getLogbooksByUserId(userDetails);

        if (logbookResponses != null && !logbookResponses.isEmpty()) {
            return ResponseEntity.ok(logbookResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("")
    @Operation(summary = "로그북 수정하기", description = "나의 로그를 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> updateLog(
            @RequestBody LogbookRequest logbookRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetailsImpl);
        if (response != null) return response;

        LogbookResponse logbookResponse = logbookService.updateLogbook(logbookRequest, userDetailsImpl);

        if (logbookResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(logbookResponse);
        }
    }

    @DeleteMapping("{logbookId}")
    @Operation(summary = "로그북 삭제하기", description = "나의 로그북을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<CommonResponse> deleteLogbook(
            @PathVariable("logbookId") Long logbookId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return logbookService.deleteLog(logbookId, userDetails);
    }

    @DeleteMapping("all")
    @Operation(summary = "(테스트용) 모든 로그북 삭제하기", description = "모든 로그북을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public void deleteAllLogbook() {
        logbookService.deleteAll();
    }

    @PostMapping("like/{logbookId}")
    @Operation(summary = "좋아요 로그북 추가", description = "로그북에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LogbookResponse> likeLogbook(@PathVariable Long logbookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return response;

        LogbookResponse logbookResponse = likeLogbookService.createLike(logbookId, userDetails);
        return ResponseEntity.ok(logbookResponse);
    }

    @GetMapping("like")
    @Operation(summary = "좋아요한 로그북 가져오기", description = "좋아요한 로그북을 가져옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LogbookResponse>> getLikeLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        List<LogbookResponse> logbookResponses = likeLogbookService.getLikeLogbooks(userDetails);

        if (logbookResponses != null && !logbookResponses.isEmpty()) {
            return ResponseEntity.ok(logbookResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("like/{logbookId}")
    @Operation(summary = "좋아요한 로그북 취소하기", description = "좋아요한 로그북을 취소합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<String> cancelLikeLogbooks(@PathVariable("logbookId") Long logbookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<LogbookResponse> response = checkUserDetailsAndRespond(userDetails);
        if (response != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        try {
            likeLogbookService.cancelLike(logbookId, userDetails);
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 취소에 실패했습니다.");
        }
    }
}
