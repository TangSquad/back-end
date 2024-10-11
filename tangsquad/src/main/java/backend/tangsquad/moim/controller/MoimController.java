package backend.tangsquad.moim.controller;
import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.like.dto.request.LikeMoimRequest;
import backend.tangsquad.like.service.LikeMoimService;
import backend.tangsquad.moim.dto.request.MoimLeaderUsernameRequest;
import backend.tangsquad.moim.dto.response.*;
import backend.tangsquad.moim.dto.request.MoimCreateRequest;
import backend.tangsquad.moim.dto.request.MoimLeaderRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import backend.tangsquad.moim.service.MoimService;
import backend.tangsquad.common.service.UserService;
import backend.tangsquad.swagger.global.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/moim")
@RestController
@RequiredArgsConstructor
@Tag(name = "Moim", description = "모임 관련 API")
public class MoimController {

    private final MoimService moimService;
    private final UserService userService;
    private final LikeMoimService likeMoimService;

    // Create a new moim
    @PostMapping
    @Operation(
            summary = "모임 생성",
            description = "새로운 모임을 생성합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "모임 생성 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<MoimResponse> createMoim(
            @RequestBody MoimCreateRequest moimCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MoimResponse moimCreateResponse = moimService.createMoim(moimCreateRequest, userDetails);

        if (moimCreateResponse != null) {
            return ResponseEntity.ok(moimCreateResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/join")
    @Operation(
            summary = "모임 가입",
            description = "사용자가 기존 모임에 가입합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    public ResponseEntity<MoimJoinResponse> joinMoim(
            @RequestParam Long moimId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MoimJoinResponse moimJoinResponse = moimService.joinMoim(moimId, userDetails);

        if (moimJoinResponse != null) {
            return ResponseEntity.ok(moimJoinResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "모임 목록 조회",
            description = "모든 모임의 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<MoimResponse>> getAllMoims() {

        List<MoimResponse> moimResponses = moimService.getAllMoims();

        if (moimResponses != null) {
            return ResponseEntity.ok(moimResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/joined")
    @Operation(
            summary = "가입한 모임 목록 조회",
            description = "사용자가 가입한 모임 목록을 조회합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록한 모임 목록 조회 성공", content = @Content(schema = @Schema(implementation = MoimResponse.class))),
            @ApiResponse(responseCode = "404", description = "등록한 모임이 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<MoimResponse>> getRegisteredMoims(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<MoimResponse> moimReadResponses = moimService.getRegisteredMoims(userDetails);

        if (moimReadResponses != null) {
            return ResponseEntity.ok(moimReadResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    @GetMapping("")
    @Operation(summary = "사용자가 생성한 모임 불러오기", description = "사용자가 생성한 모임들을 불러옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<MoimResponse>> getMyMoims(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<MoimResponse> moimReadResponses = moimService.getMoims(userDetails);

        if (moimReadResponses != null) {
            return ResponseEntity.ok(moimReadResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("")
    @Operation(summary = "모임 수정하기", description = "사용자의 모임을 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<MoimResponse> updateMoim(
            @RequestBody MoimUpdateRequest moimUpdateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MoimResponse moimResponse = moimService.updateMoim(moimUpdateRequest, userDetails);

        if (moimResponse != null) {
            return ResponseEntity.ok(moimResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update-leader")
    @Operation(summary = "모임 소유자 수정하기", description = "모임의 소유자를 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<MoimLeaderResponse> updateMoimLeader(
            @RequestBody MoimLeaderRequest moimLeaderRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MoimLeaderResponse moimLeaderResponse = moimService.updateMoimLeader(userDetails, moimLeaderRequest);

        if (moimLeaderResponse != null) {
            return ResponseEntity.ok(moimLeaderResponse);
        } else {
            return null;
        }
    }

    @PutMapping("/update-leader-by-name")
    @Operation(summary = "모임 소유자 수정하기 (사용자 이름)", description = "모임의 소유자를 사용자 이름으로 수정합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<MoimLeaderUsernameResponse> updateMoimLeaderByUsername(
            @RequestBody MoimLeaderUsernameRequest moimLeaderUsernameRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MoimLeaderUsernameResponse moimLeaderUsernameResponse = moimService.updateMoimLeaderByUsername(userDetails, moimLeaderUsernameRequest);

        if (moimLeaderUsernameRequest != null) {
            return ResponseEntity.ok(moimLeaderUsernameResponse);
        } else {
            return null;
        }
    }




    @DeleteMapping("/{moimId}")
    @Operation(summary = "모임 삭제하기", description = "사용자의 모임을 삭제합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<?> deleteMoim(
            @PathVariable("moimId") Long moimId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean deletedWell = moimService.deleteMoim(moimId, userDetails);

        if (deletedWell) {
            return ResponseEntity.ok(CommonResponse.success());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete Moim.");
        }
    }


    @PostMapping("like/{moimId}")
    @Operation(summary = "좋아요 모임 추가", description = "로그북에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LikeMoimRequest> likeMoim(@PathVariable Long moimId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        LikeMoimRequest likeMoimRequest = likeMoimService.createLike(moimId, userDetails);

        return ResponseEntity.ok(likeMoimRequest);
    }

    @GetMapping("like")
    @Operation(summary = "좋아요한 모임 가져오기", description = "좋아요한 모임을 가져옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<MoimResponse>> getLikeMoims(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<MoimResponse> moimResponses = likeMoimService.getLikeMoims(userDetails);

        if (moimResponses != null) {
            return ResponseEntity.ok(moimResponses);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
