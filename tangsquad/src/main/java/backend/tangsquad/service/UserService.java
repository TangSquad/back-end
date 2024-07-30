package backend.tangsquad.service;

import backend.tangsquad.domain.Certification;
import backend.tangsquad.domain.User;
import backend.tangsquad.domain.UserProfile;
import backend.tangsquad.domain.Equipment;
import backend.tangsquad.dto.request.RegisterRequestDto;
import backend.tangsquad.repository.CertificationRepository;
import backend.tangsquad.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User registerUser(@Valid RegisterRequestDto registerRequestDto) {
        String encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());

        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setUsername(registerRequestDto.getUsername());
        user.setPhone(registerRequestDto.getPhone());
        user.setName(registerRequestDto.getName());
        user.setRole("ROLE_USER");

        // Create and set UserProfile
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);

        // Create and set Equipment
        Equipment equipment = new Equipment();
        equipment.setUserProfile(userProfile);

        userProfile.setEquipment(equipment);
        user.setUserProfile(userProfile);

        User savedUser = userRepository.save(user);

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

        return savedUser;
    }
}
