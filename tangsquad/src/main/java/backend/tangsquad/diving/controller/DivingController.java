package backend.tangsquad.diving.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.diving.dto.response.DivingJoinResponse;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.entity.Location;
import backend.tangsquad.diving.service.DivingService;
import backend.tangsquad.like.dto.request.LikeDivingRequest;
import backend.tangsquad.like.dto.response.LikeDivingResponse;
import backend.tangsquad.like.service.LikeDivingService;
import backend.tangsquad.moim.dto.response.MoimResponse;
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
import org.springframework.security.core.parameters.P;
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
            @ApiResponse(responseCode = "201", description = "다이빙 생성 성공", content = @Content(schema = @Schema(implementation = DivingResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<DivingResponse> createDiving(
            @RequestBody DivingRequest divingRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        DivingResponse divingResponse = divingService.createDiving(divingRequest, userDetails);

        if (divingResponse != null ) {
            return ResponseEntity.ok(divingResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/recent")
    @Operation(
            summary = "신규 다이빙 목록 조회",
            description = "최근 3개 다이빙의 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다이빙 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<DivingResponse>> getRecentDivings() {
        List<DivingResponse> recentDivingResponses = divingService.getRecentDivings();

        if (recentDivingResponses != null) {
            return ResponseEntity.ok(recentDivingResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("popular")
    @Operation(
            summary = "이 달의 다이빙 스팟",
            description = "사용자들이 가장 많이 방문한 스팟을 보여줍니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다이빙 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<Location>> getPopularSpots() {
        List<Location> popularSpots = divingService.getPopularSpots();

        if (popularSpots != null) {
            return ResponseEntity.ok(popularSpots);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping("/all")
    @Operation(
            summary = "다이빙 목록 조회",
            description = "모든 다이빙의 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다이빙 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<DivingResponse>> getAllDivings() {

        List<DivingResponse> divingResponses = divingService.getAllDivings();

        if (divingResponses != null) {
            return ResponseEntity.ok(divingResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/join/{divingId}")
    @Operation(
            summary = "diving 가입",
            description = "다이빙에 가입합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록한 다이빙 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록한 다이빙이 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<DivingJoinResponse> joinDiving(@PathVariable("divingId") Long divingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DivingJoinResponse divingjoinResponse = divingService.joinDiving(divingId, userDetails);

        if (divingjoinResponse != null) {
            return ResponseEntity.ok(divingjoinResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/joined")
    @Operation(
            summary = "가입한 diving 목록 조회",
            description = "사용자가 가입한 다이빙 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록한 다이빙 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록한 다이빙이 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<DivingResponse>> getRegisteredDivings(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<DivingResponse> divingResponses = divingService.getRegisteredDivings(userDetails);

        if (divingResponses != null) {
            return ResponseEntity.ok(divingResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("users/{divingId}")
    @Operation(
            summary = "diving에 가입한 사용자 목록 조회",
            description = "다이빙에 가입된 다이버 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록한 다이빙 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록한 다이빙이 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<DivingJoinResponse> getRegisteredUsers(@PathVariable("divingId") Long divingId) {
        DivingJoinResponse divingJoinResponse = divingService.getMemberList(divingId);

        if (divingJoinResponse != null) {
            return ResponseEntity.ok(divingJoinResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("")
    @Operation(summary = "내 다이빙 불러오기", description = "나의 다이빙들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<DivingResponse>> getMyDivings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<DivingResponse> divingResponses = divingService.getMyDivings(userDetails.getId());

        if (divingResponses != null) {
            return ResponseEntity.ok(divingResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{divingId}")
    @Operation(summary = "다이빙 불러오기", description = "특정 다이빙을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<DivingResponse> getDiving(@PathVariable("divingId") Long divingId) {
        DivingResponse divingResponse = divingService.getDiving(divingId);

        if (divingResponse != null) {
            return ResponseEntity.ok(divingResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("")
    @Operation(summary = "다이빙 수정하기", description = "나의 다이빙 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<DivingResponse> updateDiving(
            @PathVariable Long divingId,
            @RequestBody DivingRequest divingRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DivingResponse divingResponse = divingService.updateDiving(divingId, divingRequest, userDetails);

        if (divingResponse != null ) {
            return ResponseEntity.ok(divingResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/{divingId}")
    @Operation(summary = "다이빙 삭제하기", description = "나의 다이빙을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<CommonResponse> deleteLog(
            @PathVariable("divingId") Long divingId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        boolean deletedWell = divingService.deleteDiving(divingId, userDetailsImpl);

        if (deletedWell) {
            return ResponseEntity.ok(CommonResponse.success());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("all")
    @Operation(summary = "(테스트용) 모든 다이빙 삭제하기", description = "모든 다이빙을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public void deleteAllDivings() {
        divingService.deleteAll();
    }

    @PostMapping("like/{divingId}")
    @Operation(summary = "좋아요 다이빙 추가", description = "다이빙에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LikeDivingResponse> likeDiving(@PathVariable Long divingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LikeDivingResponse likeDivingResponse = likeDivingService.createLike(divingId, userDetails);
        return ResponseEntity.ok(likeDivingResponse);
    }

    @GetMapping("like")
    @Operation(summary = "좋아요한 다이빙 가져오기", description = "좋아요한 로그북을 가져옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<DivingResponse>> getLikeDivings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<DivingResponse> divingResponses = likeDivingService.getLikeDivings(userDetails);

        if (divingResponses != null) {
            return ResponseEntity.ok(divingResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("like/{divingId}")
    @Operation(summary = "좋아요한 다이빙 취소하기", description = "좋아요한 다이빙을 취소합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<String> cancelLikeDivings(@PathVariable("divingId") Long divingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            likeDivingService.cancelLike(divingId, userDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok("좋아요 취소 성공");
    }
}
