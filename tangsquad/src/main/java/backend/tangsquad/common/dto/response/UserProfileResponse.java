package backend.tangsquad.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String profileImageUrl;
    private String name;
    private String nickname;
    private Integer clubCount;
    private Integer divingCount;
    private Integer logBookCount;
}
