package backend.tangsquad.service;

import backend.tangsquad.domain.Certification;
import backend.tangsquad.domain.User;
import backend.tangsquad.domain.UserProfile;
import backend.tangsquad.domain.Equipment;
import backend.tangsquad.dto.request.RegisterRequestDto;
import backend.tangsquad.dto.response.RegisterResponse;
import backend.tangsquad.dto.response.WithdrawResponse;
import backend.tangsquad.repository.CertificationRepository;
import backend.tangsquad.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, CertificationRepository certificationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.certificationRepository = certificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse registerUser(@Valid RegisterRequestDto registerRequestDto) {
        // 이메일 또는 사용자 이름 중복 확인
        Optional<User> existingUserByEmail = userRepository.findByEmail(registerRequestDto.getEmail());
        if (existingUserByEmail.isPresent()) {
            return new RegisterResponse(false, "Email already exists.", null);
        }

        Optional<User> existingUserByUsername = userRepository.findByUsername(registerRequestDto.getUsername());
        if (existingUserByUsername.isPresent()) {
            return new RegisterResponse(false, "Username already exists.", null);
        }

        Optional<User> existingUserByPhone = userRepository.findByPhone(registerRequestDto.getPhone());
        if (existingUserByPhone.isPresent()) {
            return new RegisterResponse(false, "Phone number already exists.", null);
        }

        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());

        // 새 사용자 생성
        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setUsername(registerRequestDto.getUsername());
        user.setPhone(registerRequestDto.getPhone());
        user.setName(registerRequestDto.getName());
        user.setRole("ROLE_USER");

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

        // 자격증 추가
        if (registerRequestDto.getCertifications() != null) {
            registerRequestDto.getCertifications().forEach(certificationDto -> {
                Certification certification = new Certification();
                certification.setUser(savedUser);
                certification.setOrganization(certificationDto.getOrganization());
                certification.setGrade(certificationDto.getGrade());
                certification.setImageUrl(certificationDto.getImageUrl());
                certificationRepository.save(certification);
            });
        }

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

    public boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
