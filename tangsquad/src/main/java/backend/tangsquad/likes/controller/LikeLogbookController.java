package backend.tangsquad.likes.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.likes.dto.request.LikeLogbookRequest;
import backend.tangsquad.likes.entity.LikeLogbook;
import backend.tangsquad.likes.service.LikeLogbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/like")
@RestController
@Tag(name = "Like", description = "좋아요 관련 API")
@AllArgsConstructor
public class LikeLogbookController {

    private final LikeLogbookService likeLogbookService;
    @PostMapping("{logbookId}")
    @Operation(summary = "좋아요 로그북 추가", description = "로그북에 좋아요를 추가합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<LikeLogbookRequest> like(@PathVariable Long logbookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LikeLogbookRequest likeLogbookRequest = likeLogbookService.createLike(logbookId, userDetails);
        return ResponseEntity.ok(likeLogbookRequest);
    }

    @GetMapping("")
    @Operation(summary = "좋아요한 로그북 가져오기", description = "좋아요한 로그북을 가져옵니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ResponseEntity<List<LikeLogbookRequest>> getLikeLogbooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Get the list of liked logbooks as LikeLogbookRequest DTOs
        List<LikeLogbookRequest> likeLogbooks = likeLogbookService.getLikeLogbooks(userDetails);

        // Return the list wrapped in ResponseEntity
        return ResponseEntity.ok(likeLogbooks);
    }

}
