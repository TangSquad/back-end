package backend.tangsquad.like.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeLogbookRequest {
    private Long userId;
    private Long logbookid;
}
