package backend.tangsquad.like.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class LikeLogbookResponse {
    private Long userId;
    private Long logbookid;

    @Builder
    public LikeLogbookResponse(Long userId, Long logbookid) {
        this.userId = userId;
        this.logbookid = logbookid;
    }
}
