package backend.tangsquad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoimLeaderUpdateRequest {
    private Long id;
    private Long userId;
}
