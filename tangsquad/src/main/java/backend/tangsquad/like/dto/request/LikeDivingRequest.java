package backend.tangsquad.like.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDivingRequest {
    private Long userId;
    private Long divingId;
}
