package backend.tangsquad.logbook.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.logbook.dto.request.LogCreateRequest;
import backend.tangsquad.logbook.dto.request.LogRequest;
import backend.tangsquad.logbook.dto.request.LogUpdateRequest;
import backend.tangsquad.logbook.dto.response.LogResponse;
import backend.tangsquad.logbook.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/log")
@RestController
@RequiredArgsConstructor
@Tag(name = "Log", description = "로그 관련 API")
public class LogController {

    private final LogService logService;
    @PostMapping("")
    @Operation(
            summary = "로그 생성",
            description = "새로운 로그를 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<LogResponse> createLog(
            @RequestBody LogCreateRequest logCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LogResponse logResponse = logService.save(logCreateRequest, userDetails);

        if (logResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("all")
    @Operation(
            summary = "모든 로그들을 가져오기",
            description = "모든 로그들을 가져옵니다",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<List<LogResponse>> getAllLogs() {
        List<LogResponse> logResponses = logService.getAllLogs();

        if (logResponses != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }


    @GetMapping("logbook/{logbookId}")
    @Operation(
            summary = "특정 로그북에 대한 로그들을 가져오기",
            description = "특정 로그북에 대한 로그들을 가져옵니다",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<List<LogResponse>> getLogsInLogbook(
            @PathVariable("logbookId") Long logbookId) {
        List<LogResponse> logResponses = logService.getLogsInLogbook(logbookId);
        if (logResponses != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("{logId}")
    @Operation(
            summary = "로그 아이디로 로그를 가져오기",
            description = "로그 아이디로 로그를 가져옵니다",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<LogResponse> getLog(
            @PathVariable("logId") Long logId) {
        LogResponse logResponse = logService.getLog(logId);

        if (logResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PutMapping("update")
    @Operation(
            summary = "로그를 수정하기",
            description = "로그를 수정합니다",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<LogResponse> updateLog(
            @RequestBody LogUpdateRequest logUpdateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        LogResponse logResponse = logService.updateLog(logUpdateRequest, userDetails);

        if (logResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("{logId}")
    @Operation(
            summary = "로그 삭제하기",
            description = "로그를 삭제합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    public ResponseEntity<LogResponse> deleteLog(
            @PathVariable("logId") Long logId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        LogResponse logResponse = logService.delete(logId, userDetails);
        if (logResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(logResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}