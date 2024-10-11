package backend.tangsquad.moim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoimLeaderResponse {
    private Long id;
    private Long userId;

    @Builder
    public MoimLeaderResponse(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }
}
