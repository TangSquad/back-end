package backend.tangsquad.moim.dto.response;


import backend.tangsquad.common.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class MoimLeaderUsernameResponse {
    private Long moimId;
    private User user;

    @Builder
    public MoimLeaderUsernameResponse(Long moimId, User user) {
        this.moimId = moimId;
        this.user = user;
    }
}
