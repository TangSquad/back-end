package backend.tangsquad.auth.service;

import backend.tangsquad.auth.dto.response.KakaoInfo;
import backend.tangsquad.common.dto.request.LoginRequestDto;
import backend.tangsquad.common.dto.request.RegisterRequestDto;
import backend.tangsquad.common.dto.response.JwtResponseDto;
import backend.tangsquad.common.dto.response.RegisterResponse;
import backend.tangsquad.common.entity.Equipment;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.common.entity.UserProfile;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.common.service.AuthService;
import backend.tangsquad.common.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;

    @Transactional
    public JwtResponseDto kakaoLogin(String accessToken) {
        KakaoInfo kakaoInfo = getKakaoInfo(accessToken);

        // 휴대폰 번호나 이메일을 통해 유저를 가져옴
        Optional<User> userOptional = findUserByPhoneOrEmail(kakaoInfo.getPhone_number(), kakaoInfo.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String userPlatform = Optional.ofNullable(user.getPlatform()).orElse("");

            // 플랫폼 검증
            if (!userPlatform.equals("kakao")) {
                throw new IllegalArgumentException("다른 플랫폼으로 가입한 사용자입니다.");
            }

            // 로그인 성공
            return authService.authenticateUser(new LoginRequestDto(kakaoInfo.getEmail(), "kakao"));
        }

        // 회원가입 로직
        return registerAndLoginUser(kakaoInfo);
    }

    private Optional<User> findUserByPhoneOrEmail(String phoneNumber, String email) {
        // 휴대폰 번호로 먼저 검색하고, 없으면 이메일로 검색
        return userRepository.findByPhone(phoneNumber)
                .or(() -> userRepository.findByEmail(email));
    }

    private JwtResponseDto registerAndLoginUser(KakaoInfo kakaoInfo) {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setEmail(kakaoInfo.getEmail());
        registerRequestDto.setName(kakaoInfo.getName());
        registerRequestDto.setPhone(kakaoInfo.getPhone_number());
        registerRequestDto.setPlatform("kakao");

        // 회원가입 처리
        userService.registerUser(registerRequestDto);

        // 회원가입 후 로그인 처리
        return authService.authenticateUser(new LoginRequestDto(registerRequestDto.getEmail(), "kakao"));
    }

    public KakaoInfo getKakaoInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> accountInfoResponse = rt.postForEntity("https://kapi.kakao.com/v2/user/me", accountInfoRequest, String.class);

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();

        KakaoInfo kakaoInfo = new KakaoInfo();

        try {
            JsonNode rootNode = objectMapper.readTree(accountInfoResponse.getBody());

            // 필요한 정보 추출
            String name = rootNode.path("kakao_account").path("name").asText();
            String email = rootNode.path("kakao_account").path("email").asText();
            String rawPhoneNumber = rootNode.path("kakao_account").path("phone_number").asText();

            String phoneNumber = formatPhoneNumber(rawPhoneNumber);

            // KakaoInfo 객체에 설정
            kakaoInfo.setName(name);
            kakaoInfo.setEmail(email);
            kakaoInfo.setPhone_number(phoneNumber);
        } catch (Exception e) {
            throw new IllegalArgumentException("카카오 정보를 가져오는데 실패했습니다.");
        }
        return kakaoInfo;
    }

    // 전화번호 변환 메서드
    private String formatPhoneNumber(String rawPhoneNumber) {
        // "+82 "를 "0"으로 바꾸고, 나머지 "-"를 제거
        return rawPhoneNumber.replace("+82 ", "0").replace("-", "");
    }
}
