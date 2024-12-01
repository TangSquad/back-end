package backend.tangsquad.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "프로필 수정", requiredProperties = {"nickname"})
public class ProfileEditRequest {

    @Schema(description = "닉네임", nullable = true)
    private String nickname;

    @Schema(description = "프로필 이미지 URL", nullable = true)
    private String profileImageUrl;

    @Schema(description = "인증 조직 ID", nullable = true)
    private Long certOrganizationId;

    @Schema(description = "인증 레벨 ID", nullable = true)
    private Long certLevelId;

    @Schema(description = "인증서 이미지 URL", nullable = true)
    private String certificateImageUrl;

    @Schema(description = "자기소개", nullable = true)
    private String introduction;

    @Schema(description = "링크", nullable = true)
    private String link;

    @Schema(description = "소속", nullable = true)
    private String affiliation;

    @Schema(description = "다이빙 로그북 공개 여부", nullable = true)
    private Boolean isLogbookOpen;

    @Schema(description = "좋아요 공개 여부", nullable = true)
    private Boolean isLikeOpen;

    @Schema(description = "장비 공개 여부", nullable = true)
    private Boolean isEquipmentOpen;

    @Schema(description = "키", nullable = true)
    private String height;

    @Schema(description = "몸무게", nullable = true)
    private String weight;

    @Schema(description = "수트", nullable = true)
    private String suit;

    @Schema(description = "신발", nullable = true)
    private String shoes;

    @Schema(description = "웨이트 벨트", nullable = true)
    private String weightBelt;

    @Schema(description = "마스크", nullable = true)
    private String mask;

    @Schema(description = "BCD", nullable = true)
    private String bc;
}
