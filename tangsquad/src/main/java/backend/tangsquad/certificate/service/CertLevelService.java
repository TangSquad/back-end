package backend.tangsquad.certificate.service;

import backend.tangsquad.certificate.entity.CertLevel;
import backend.tangsquad.certificate.repository.CertLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertLevelService {

    private final CertLevelRepository certLevelRepository;

    @Transactional(readOnly = true)
    public CertLevel getCertLevel(Long certLevelId) {
        return certLevelRepository.findById(certLevelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자격증 단계가 존재하지 않습니다. id=" + certLevelId));
    }

    @Transactional(readOnly = true)
    public List<CertLevel> getCertLevelByCertOrganizationId(Long certOrganizationId) {
        return certLevelRepository.findByCertOrganizationId(certOrganizationId);
    }

}
