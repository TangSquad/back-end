package backend.tangsquad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String profileImageUrl;
    private String name;
    private String username;
    private Integer clubCount;
    private Integer divingCount;
    private Integer logBookCount;
}
