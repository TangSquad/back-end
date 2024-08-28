package backend.tangsquad.certificate.repository;

import backend.tangsquad.certificate.entity.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCertificateRepository extends JpaRepository<UserCertificate, Long>{
    Optional<UserCertificate> findByUserId(Long userId);
}
