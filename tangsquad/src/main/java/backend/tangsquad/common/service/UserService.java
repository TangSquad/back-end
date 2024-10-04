package backend.tangsquad.common.service;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.common.entity.UserProfile;
import backend.tangsquad.common.entity.Equipment;
import backend.tangsquad.common.dto.request.RegisterRequestDto;
import backend.tangsquad.common.dto.response.RegisterResponse;
import backend.tangsquad.common.dto.response.WithdrawResponse;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogbookRepository;
import backend.tangsquad.util.RandomNickname;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomNickname randomNickname;

    @Transactional
    public RegisterResponse registerUser(@Valid RegisterRequestDto registerRequestDto) {
        // 이메일 또는 사용자 이름 중복 확인
        Optional<User> existingUserByEmail = userRepository.findByEmail(registerRequestDto.getEmail());
        if (existingUserByEmail.isPresent()) {
            return new RegisterResponse(false, "Email already exists.", null);
        }


        Optional<User> existingUserByPhone = userRepository.findByPhone(registerRequestDto.getPhone());
        if (existingUserByPhone.isPresent()) {
            return new RegisterResponse(false, "Phone number already exists.", null);
        }

        String nickname = randomNickname.generate();

        while (userRepository.existsByNickname(nickname)) {
            nickname = randomNickname.generate();
        }

        String encodedPassword;
        if (registerRequestDto.getPassword() == null) {
            encodedPassword = passwordEncoder.encode(registerRequestDto.getPlatform());
        } else {
            // 패스워드 암호화
            encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());
        }

        // 새 사용자 생성
        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setNickname(nickname);
        user.setPhone(registerRequestDto.getPhone());
        user.setName(registerRequestDto.getName());
        user.setRole("ROLE_USER");
        if (registerRequestDto.getPlatform() != null) {
            user.setPlatform(registerRequestDto.getPlatform());
        }

        // UserProfile 생성 및 설정
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);

        // Equipment 생성 및 설정
        Equipment equipment = new Equipment();
        equipment.setUserProfile(userProfile);

        userProfile.setEquipment(equipment);
        user.setUserProfile(userProfile);

        // 사용자 저장
        User savedUser = userRepository.save(user);

        // 성공 응답 반환
        return new RegisterResponse(true, "User registered successfully", savedUser.getId());
    }

    @Transactional
    public WithdrawResponse deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            return new WithdrawResponse(true, "User deleted successfully.");
        } catch (Exception e) {
            return new WithdrawResponse(false, "User deletion failed.");
        }
    }

    @Transactional
    public boolean updatePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public String getUserPlatform(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPlatform();
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean isNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public boolean isUserUpdated(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // create 시간에 3초 여유
            return user.getUpdatedAt().isAfter(user.getCreatedAt().plusSeconds(3));
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    public Optional<List<Long>> getLikedLogbookIds(Long userId) {
        // Fetch logbook IDs that the user has liked
        return userRepository.findLikedLogbooksById(userId);
    }



    public User findByName(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

}
