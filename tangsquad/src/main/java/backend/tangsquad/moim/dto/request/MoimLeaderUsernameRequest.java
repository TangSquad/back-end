package backend.tangsquad.moim.dto.request;


import backend.tangsquad.common.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class MoimLeaderUsernameRequest {
    private Long moimId;
    private User user;

    @Builder
    public MoimLeaderUsernameRequest(Long moimId, User user) {
        this.moimId = moimId;
        this.user = user;
    }
}
