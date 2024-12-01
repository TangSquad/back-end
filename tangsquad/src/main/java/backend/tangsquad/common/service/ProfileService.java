package backend.tangsquad.common.service;

import backend.tangsquad.certificate.entity.UserCertificate;
import backend.tangsquad.certificate.repository.UserCertificateRepository;
import backend.tangsquad.certificate.service.UserCertificateService;
import backend.tangsquad.common.entity.Equipment;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.common.entity.UserProfile;
import backend.tangsquad.common.dto.request.ProfileEditRequest;
import backend.tangsquad.common.dto.response.ProfileEditResponse;
import backend.tangsquad.common.dto.response.UserEquipmentResponse;
import backend.tangsquad.common.dto.response.UserIntroductionResponse;
import backend.tangsquad.common.dto.response.UserProfileResponse;
import backend.tangsquad.common.repository.UserProfileRepository;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.service.DivingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserCertificateService userCertificateService;
    private final UserCertificateRepository userCertificateRepository;
    private final DivingService divingService;
    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = getUserById(userId);
        UserCertificate userCertificate = userCertificateRepository.findByUserId(userId).orElse(null);

        return new UserProfileResponse(
                userId,
                user.getUserProfile().getProfileImageUrl(),
                user.getName(),
                user.getNickname(),
                0, // 다양한 데이터에 대한 추가 처리가 필요할 경우 이 부분에서 수행
                0,
                0,
                userCertificate != null ? userCertificate.getCertificate().getCertLevel().getName() : "",
                userCertificate != null ? userCertificate.getImageUrl() : "",
                true,
                true,
                true
        );
    }

    @Transactional(readOnly = true)
    public UserIntroductionResponse getUserIntroduction(Long userId) {
        UserProfile profile = getUserById(userId).getUserProfile();
        List< DivingResponse> divingResponses = divingService.getMyDivings(userId);

        // 마지막 객체 가져오기 (리스트가 비어있지 않을 경우)
        DivingResponse lastDivingResponse =
                divingResponses.isEmpty() ? null : divingResponses.get(divingResponses.size() - 1);

        return new UserIntroductionResponse(
                profile.getIntroduction(),
                profile.getUrl(),
                profile.getAffiliation(),
                lastDivingResponse
        );
    }

    @Transactional
    public UserEquipmentResponse getUserEquipment(Long userId) {
        User user = getUserById(userId);  // user를 한 번만 조회
        UserProfile userProfile = user.getUserProfile();  // 이미 로드된 userProfile 사용

        Equipment equipment = userProfile.getEquipment();

        if (equipment == null) {  // 장비 정보가 없을 경우
            equipment = new Equipment();
            equipment.setUserProfile(userProfile);
            userProfile.setEquipment(equipment);
            userProfileRepository.save(userProfile);  // userProfile과 함께 저장
        }

        return new UserEquipmentResponse(
                equipment.getHeight(),
                equipment.getWeight(),
                equipment.getSuit(),
                equipment.getWeightBelt(),
                equipment.getBc(),
                equipment.getShoes(),
                equipment.getMask()
        );
    }

    @Transactional
    public ProfileEditResponse updateProfile(Long userId, ProfileEditRequest request) {
        User user = getUserById(userId);

        updateUserData(user, request);
        updateUserCertificate(userId, request.getCertOrganizationId(), request.getCertLevelId(), request.getCertificateImageUrl());

        return createProfileEditResponse(user);
    }

    @Transactional
    public Boolean setAdditionalInfo(Long userId, String nickname, String profileImage, Long organizationId, Long levelId, String certificateImageUrl) {
        User user = getUserById(userId);

        updateNickname(user, nickname);
        updateProfileImage(user.getUserProfile(), profileImage);

        if (organizationId != null && levelId != null && certificateImageUrl != null) {
            userCertificateService.createUserCertificate(userId, organizationId, levelId, certificateImageUrl);
        }

        return true;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
    }

    private void updateUserData(User user, ProfileEditRequest request) {
        updateNickname(user, request.getNickname());
        updateProfileImage(user.getUserProfile(), request.getProfileImageUrl());
        updateProfileDetails(user.getUserProfile(), request);
        updateEquipment(user.getUserProfile().getEquipment(), request);
    }

    private void updateNickname(User user, String nickname) {
        if (nickname != null && !nickname.equals(user.getNickname())) {
            if (userRepository.existsByNickname(nickname)) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setNickname(nickname);
        }
    }

    private void updateProfileImage(UserProfile profile, String profileImageUrl) {
        if (profileImageUrl != null) {
            profile.setProfileImageUrl(profileImageUrl);
        }
    }

    private void updateUserCertificate(Long userId, Long organizationId, Long levelId, String certificateImageUrl) {
        if (organizationId != null && levelId != null && certificateImageUrl != null) {
            userCertificateService.updateUserCertificate(userId, organizationId, levelId, certificateImageUrl);
        }
    }

    private void updateProfileDetails(UserProfile profile, ProfileEditRequest request) {
        if (request.getIntroduction() != null) {
            profile.setIntroduction(request.getIntroduction());
        }
        if (request.getLink() != null) {
            profile.setUrl(request.getLink());
        }
        if (request.getAffiliation() != null) {
            profile.setAffiliation(request.getAffiliation());
        }
        // 주석 처리된 부분에 대한 로직도 필요시 추가
    }

    private void updateEquipment(Equipment equipment, ProfileEditRequest request) {
        if (request.getHeight() != null) {
            equipment.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            equipment.setWeight(request.getWeight());
        }
        if (request.getSuit() != null) {
            equipment.setSuit(request.getSuit());
        }
        if (request.getShoes() != null) {
            equipment.setShoes(request.getShoes());
        }
        if (request.getWeightBelt() != null) {
            equipment.setWeightBelt(request.getWeightBelt());
        }
        if (request.getMask() != null) {
            equipment.setMask(request.getMask());
        }
        if (request.getBc() != null) {
            equipment.setBc(request.getBc());
        }
    }

    private ProfileEditResponse createProfileEditResponse(User user) {
        UserProfile profile = user.getUserProfile();
        Equipment equipment = profile.getEquipment();
        Optional<UserCertificate> userCertificate = userCertificateRepository.findByUserId(user.getId());

        if (userCertificate.isEmpty()) {
            return new ProfileEditResponse(
                    user.getName(),
                    user.getNickname(),
                    profile.getProfileImageUrl(),
                    null,
                    null,
                    profile.getIntroduction(),
                    profile.getUrl(),
                    profile.getAffiliation(),
                    false, // isLogbookOpen
                    false, // isLikeOpen
                    false, // isEquipmentOpen
                    equipment.getHeight(),
                    equipment.getWeight(),
                    equipment.getSuit(),
                    equipment.getShoes(),
                    equipment.getWeightBelt(),
                    equipment.getMask(),
                    equipment.getBc()
            );
        } else {

            return new ProfileEditResponse(
                    user.getName(),
                    user.getNickname(),
                    profile.getProfileImageUrl(),
                    userCertificate.get().getId(),
                    userCertificate.get().getImageUrl(),
                    profile.getIntroduction(),
                    profile.getUrl(),
                    profile.getAffiliation(),
                    false, // isLogbookOpen
                    false, // isLikeOpen
                    false, // isEquipmentOpen
                    equipment.getHeight(),
                    equipment.getWeight(),
                    equipment.getSuit(),
                    equipment.getShoes(),
                    equipment.getWeightBelt(),
                    equipment.getMask(),
                    equipment.getBc()
            );
        }


    }
}
