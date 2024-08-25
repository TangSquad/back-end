package backend.tangsquad.service;

import backend.tangsquad.domain.Equipment;
import backend.tangsquad.domain.User;
import backend.tangsquad.domain.UserProfile;
import backend.tangsquad.dto.response.UserEquipmentResponse;
import backend.tangsquad.dto.response.UserIntroductionResponse;
import backend.tangsquad.dto.response.UserProfileResponse;
import backend.tangsquad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileResponse getUserProfile(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserProfileResponse(
                    user.getUserProfile().getProfileImageUrl(),
                    user.getName(),
                    user.getUsername(),
                    0,
                    0,
                    0
            );
        } else {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
    }

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

}
