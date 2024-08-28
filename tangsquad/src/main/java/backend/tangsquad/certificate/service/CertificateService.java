package backend.tangsquad.certificate.service;

import backend.tangsquad.certificate.entity.CertLevel;
import backend.tangsquad.certificate.entity.CertOrganization;
import backend.tangsquad.certificate.entity.Certificate;
import backend.tangsquad.certificate.repository.CertLevelRepository;
import backend.tangsquad.certificate.repository.CertOrganizationRepository;
import backend.tangsquad.certificate.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CertOrganizationRepository certOrganizationRepository;
    private final CertLevelRepository certLevelRepository;

    @Transactional(readOnly = true)
    public Certificate findCertificate(Long certOrganizationId, Long certLevelId) {
        return certificateRepository.findByCertOrganizationIdAndCertLevelId(certOrganizationId, certLevelId)
                .orElse(null);
    }

    @Transactional
    public Certificate createCertificate(Long certOrganizationId, Long certLevelId) {
        CertOrganization organization = certOrganizationRepository.findById(certOrganizationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자격증 단체가 존재하지 않습니다. id=" + certOrganizationId));
        CertLevel level = certLevelRepository.findById(certLevelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자격증 등급이 존재하지 않습니다. id=" + certLevelId));

        if (!organization.getId().equals(level.getCertOrganization().getId())) {
            throw new IllegalArgumentException("해당 자격증 단체와 등급이 일치하지 않습니다. 단체 id=" + certOrganizationId + ", 등급 id=" + certLevelId);
        }

        Certificate certificate = Certificate.builder()
                .certOrganization(organization)
                .certLevel(level)
                .build();
        return certificateRepository.save(certificate);
    }
}
