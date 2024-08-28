package backend.tangsquad.certificate.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "자격증 상태")
public enum CertificateState {
    REQUESTED,
    APPROVED,
    REJECTED,
    EXPIRED
}
