package backend.tangsquad.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
@Schema(description = "프로필 수정 응답")
public class ProfileEditResponse {
        private String name;
        private String username;
        private String profileImageUrl;

        private Long certificateId;
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
