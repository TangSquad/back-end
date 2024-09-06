package backend.tangsquad.moim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoimLeaderUpdateResponse {
    private Long id;
    private Long userId;
}
