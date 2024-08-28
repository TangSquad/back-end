package backend.tangsquad.service;

import backend.tangsquad.auth.jwt.JwtTokenProvider;
import backend.tangsquad.domain.RefreshToken;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LoginRequestDto;
import backend.tangsquad.dto.response.JwtResponseDto;
import backend.tangsquad.repository.RefreshTokenRepository;
import backend.tangsquad.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // methods
    public JwtResponseDto authenticateUser(LoginRequestDto loginRequestDto) throws AuthenticationException {
        try {
            // 사용자 이메일로 사용자 조회
            User user = userRepository.findByEmail(loginRequestDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getNickname(),
                            loginRequestDto.getPassword()
                    )
            );

            // 엑세스 토큰 및 리프레시 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(user.getNickname(), user.getRole());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getNickname());

            // 리프레시 토큰 저장
            refreshTokenRepository.save(user.getNickname(), refreshToken);

            return new JwtResponseDto(accessToken, refreshToken);
        } catch (UsernameNotFoundException ex) {
            // 사용자 이메일이 잘못된 경우
            throw new AuthenticationException("Invalid email or password.", ex) {
            };
        }
    }

    public JwtResponseDto refreshAccessToken(String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String nickname = jwtTokenProvider.getNickname(refreshToken);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + nickname));

        Optional<RefreshToken> storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (storedRefreshToken.isEmpty() || !storedRefreshToken.get().getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(nickname, user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(nickname);

        refreshTokenRepository.save(nickname, newRefreshToken);

        return new JwtResponseDto(newAccessToken, newRefreshToken);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

}
