package backend.tangsquad.certificate.entity;

import backend.tangsquad.certificate.Enum.CertificateState;
import backend.tangsquad.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CertificateState state;

    @Builder
    public UserCertificate(User user, Certificate certificate, String imageUrl, CertificateState state) {
        this.user = user;
        this.certificate = certificate;
        this.imageUrl = imageUrl;
        this.state = state;
    }

    public void updateCertificate(Certificate cert) {
        this.certificate = cert;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
