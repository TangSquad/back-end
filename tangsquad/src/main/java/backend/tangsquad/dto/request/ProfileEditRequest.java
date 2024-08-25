package backend.tangsquad.dto.request;

import backend.tangsquad.certificate.entity.Certificate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "프로필 수정")
public class ProfileEditRequest {

    private String username;
    private String profileImageUrl;

    private Long certOrganizationId;
    private Long certLevelId;
    private String certificateImageUrl;

    private String introduction;
    private String link;
    private String affiliation;

    private Boolean isLogbookOpen;
    private Boolean isLikeOpen;
    private Boolean isEquipmentOpen;

    private String height;
    private String weight;

    private String suit;
    private String shoes;
    private String weightBelt;
    private String mask;
    private String bc;

}
