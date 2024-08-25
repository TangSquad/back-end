package backend.tangsquad.certificate.repository;

import backend.tangsquad.certificate.entity.CertLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertLevelRepository extends JpaRepository<CertLevel, Long>{
    List<CertLevel> findByCertOrganizationId(Long certOrganizationId);
}
