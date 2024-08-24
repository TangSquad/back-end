package backend.tangsquad.certificate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "사용자 자격증 등록")
public class UserCertificateRequest {
    private Long userId;
    private Long organizationId;
    private Long levelId;
    private String certificateImage;
}
