package backend.tangsquad.like.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class LikeDivingResponse {
    private Long userId;
    private Long divingId;

    @Builder
    public LikeDivingResponse (Long userId, Long divingId){
        this.userId = userId;
        this.divingId = divingId;
    }
}
