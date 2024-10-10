package backend.tangsquad.moim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MoimLeaderRequest {
    private Long moimId;
    private Long userId;

    @Builder
    public MoimLeaderRequest(Long moimId, Long userId) {
        this.moimId = moimId;
        this.userId = userId;
    }
}
