package backend.tangsquad.certificate.repository;

import backend.tangsquad.certificate.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByCertOrganizationIdAndCertLevelId(Long certOrganizationId, Long certLevelId);
}
