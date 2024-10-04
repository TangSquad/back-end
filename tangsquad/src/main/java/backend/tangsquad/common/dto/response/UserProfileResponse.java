package backend.tangsquad.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String profileImageUrl;
    private String name;
    private String nickname;
    private Integer clubCount;
    private Integer divingCount;
    private Integer logBookCount;
    private String certificationName;
    private String certificationImageUrl;
    private Boolean isLogbookOpen;
    private Boolean isLikeOpen;
    private Boolean isEquipmentOpen;
}
