package backend.tangsquad.certificate.dto.response;

import backend.tangsquad.certificate.Enum.CertificateState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "사용자 자격증 응답")
public class UserCertificateResponse {
    private Long userCertificateId;
    private String nickname;
    private String organizationName;
    private String levelName;
    private String imageUrl;
    private CertificateState state;
}
