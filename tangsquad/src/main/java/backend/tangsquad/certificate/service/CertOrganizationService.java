package backend.tangsquad.certificate.service;

import backend.tangsquad.certificate.entity.CertOrganization;
import backend.tangsquad.certificate.repository.CertOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertOrganizationService {

    private final CertOrganizationRepository certOrganizationRepository;

    @Transactional(readOnly = true)
    public CertOrganization getCertOrganization(Long certOrganizationId) {
        return certOrganizationRepository.findById(certOrganizationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자격증 단체가 존재하지 않습니다. id=" + certOrganizationId));
    }

    @Transactional(readOnly = true)
    public List<CertOrganization> getAllCertOrganization() {
        return certOrganizationRepository.findAll();
    }
}
