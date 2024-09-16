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
import backend.tangsquad.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserCertificateService userCertificateService;
    private final UserCertificateRepository userCertificateRepository;


    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserProfileResponse(
                    userId,
                    user.getUserProfile().getProfileImageUrl(),
                    user.getName(),
                    user.getNickname(),
                    0,
                    0,
                    0
            );
        } else {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
    }

    @Transactional(readOnly = true)
    public UserIntroductionResponse getUserIntroduction(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserProfile profile = userOptional.get().getUserProfile();
            return new UserIntroductionResponse(
                    profile.getIntroduction(),
                    profile.getUrl(),
                    profile.getAffiliation(),
                    "null"
            );
        } else {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
    }

    @Transactional(readOnly = true)
    public UserEquipmentResponse getUserEquipment(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Equipment equipment = userOptional.get().getUserProfile().getEquipment();
            return new UserEquipmentResponse(
                    equipment.getHeight(),
                    equipment.getWeight(),
                    equipment.getSuit(),
                    equipment.getWeightBelt(),
                    equipment.getBc(),
                    equipment.getShoes(),
                    equipment.getMask()
            );
        } else {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
    }

    @Transactional
    public ProfileEditResponse updateProfile(Long userId, ProfileEditRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        updateNickname(user, request.getNickname());
        updateProfileImage(user.getUserProfile(), request.getProfileImageUrl());
        updateUserCertificate(userId, request.getCertOrganizationId(), request.getCertLevelId(), request.getCertificateImageUrl());
        updateProfileDetails(user.getUserProfile(), request);
        updateEquipment(user.getUserProfile().getEquipment(), request);

        return createProfileEditResponse(user);
    }

    @Transactional
    public Boolean setAdditionalInfo(Long userId, String nickname, String ProfileImage, Long organizationId, Long levelId, String certificateImageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        updateNickname(user, nickname);
        updateProfileImage(user.getUserProfile(), ProfileImage);
        if(organizationId != null && levelId != null && certificateImageUrl != null){
            userCertificateService.createUserCertificate(userId, organizationId, levelId, certificateImageUrl);
        }

        return true;
    }

    private void updateNickname(User user, String nickname) {
        if (nickname != null) {
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

    private void updateUserCertificate(Long userId, Long OrganizationId, Long levelId, String certificateImageUrl) {
        if (OrganizationId != null && levelId != null && certificateImageUrl != null) {
            userCertificateService.updateUserCertificate(userId, OrganizationId, levelId, certificateImageUrl);
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
        // 추가로 주석 처리된 부분도 필요 시 처리
        if (request.getIsLogbookOpen() != null) {
            // profile.setIsLogbookOpen(request.getIsLogbookOpen());
        }
        if (request.getIsLikeOpen() != null) {
            // profile.setIsLikeOpen(request.getIsLikeOpen());
        }
        if (request.getIsEquipmentOpen() != null) {
            // profile.setIsEquipmentOpen(request.getIsEquipmentOpen());
        }
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
        UserCertificate userCertificate = userCertificateRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("UserCertificate not found with userId " + user.getId()));

        return new ProfileEditResponse(
                user.getName(),
                user.getNickname(),
                profile.getProfileImageUrl(),
                userCertificate.getId(),
                userCertificate.getImageUrl(),
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
