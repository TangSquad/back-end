package backend.tangsquad.certificate.service;

import backend.tangsquad.certificate.Enum.CertificateState;
import backend.tangsquad.certificate.dto.response.UserCertificateResponse;
import backend.tangsquad.certificate.entity.Certificate;
import backend.tangsquad.certificate.entity.UserCertificate;
import backend.tangsquad.certificate.repository.CertificateRepository;
import backend.tangsquad.certificate.repository.UserCertificateRepository;
import backend.tangsquad.domain.User;
import backend.tangsquad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCertificateService {

    private final UserCertificateRepository userCertificateRepository;
    private final UserRepository userRepository;
    private final CertificateService certificateService;

    public UserCertificateResponse getUserCertificate(Long userId) {
        UserCertificate userCertificate = userCertificateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 자격증이 존재하지 않습니다. id=" + userId));
        return new UserCertificateResponse(userCertificate.getId(), userCertificate.getUser().getUsername(), userCertificate.getCertificate().getCertOrganization().getName(), userCertificate.getCertificate().getCertLevel().getName(), userCertificate.getImageUrl(), userCertificate.getState());
    }

    public UserCertificateResponse createUserCertificate(Long userId, Long orgId, Long levelId, String imageUrl) {
        // 이미 등록된 자격증이 있는지 확인
        if (userCertificateRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 자격증이 존재합니다. id=" + userId);
        }

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 이미지 URL입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다. id=" + userId));

        Certificate cert = certificateService.findCertificate(orgId, levelId);
        if(cert == null) {
            cert = certificateService.createCertificate(orgId, levelId);
        }

        UserCertificate userCertificate = UserCertificate.builder()
                .user(user)
                .certificate(cert)
                .imageUrl(imageUrl)
                .state(CertificateState.REQUESTED)
                .build();

        userCertificateRepository.save(userCertificate);

        return new UserCertificateResponse(userCertificate.getId(), user.getUsername(),
                cert.getCertOrganization().getName(), cert.getCertLevel().getName(),
                imageUrl, CertificateState.REQUESTED);
    }

    public void deleteUserCertificate(Long userId) {
        UserCertificate userCertificate = userCertificateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 자격증이 존재하지 않습니다. id=" + userId));
        userCertificateRepository.delete(userCertificate);
    }

    public UserCertificateResponse updateUserCertificate(Long userId, Long orgId, Long levelId, String imageUrl) {
        UserCertificate userCertificate = userCertificateRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 자격증이 존재하지 않습니다. id=" + userId));
        Certificate cert = certificateService.findCertificate(orgId, levelId);
        if(cert == null) {
            cert = certificateService.createCertificate(orgId, levelId);
        }
        userCertificate.updateCertificate(cert);
        userCertificate.updateImageUrl(imageUrl);
        userCertificate.updateState(CertificateState.REQUESTED);
        userCertificateRepository.save(userCertificate);
        return new UserCertificateResponse(userCertificate.getId(), userCertificate.getUser().getUsername(), userCertificate.getCertificate().getCertOrganization().getName(), userCertificate.getCertificate().getCertLevel().getName(), imageUrl, userCertificate.getState());
    }

}
