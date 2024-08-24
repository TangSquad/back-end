package backend.tangsquad.certificate.entity;

import backend.tangsquad.certificate.Enum.CertificateState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 자격증 단체 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private CertOrganization certOrganization;

    // 자격증 등급
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false, unique = true)
    private CertLevel certLevel;

    @Builder
    public Certificate(CertOrganization certOrganization, CertLevel certLevel) {
        this.certOrganization = certOrganization;
        this.certLevel = certLevel;
    }
}
