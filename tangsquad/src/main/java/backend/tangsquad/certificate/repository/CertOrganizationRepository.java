package backend.tangsquad.certificate.repository;

import backend.tangsquad.certificate.entity.CertOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertOrganizationRepository extends JpaRepository<CertOrganization, Long> {
}
