package backend.tangsquad.service;

import backend.tangsquad.auth.jwt.JwtTokenProvider;
import backend.tangsquad.domain.RefreshToken;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LoginRequestDto;
import backend.tangsquad.dto.request.RefreshTokenRequestDto;
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
import java.util.UUID;

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
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequestDto.getEmail()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        refreshTokenRepository.save(user.getUsername(), refreshToken);

        return new JwtResponseDto(accessToken, refreshToken);
    }

    public JwtResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {

        String refreshToken = refreshTokenRequestDto.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUserName(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Optional<RefreshToken> storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (storedRefreshToken.isEmpty() || !storedRefreshToken.get().getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(username, user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        refreshTokenRepository.save(username, newRefreshToken);

        return new JwtResponseDto(newAccessToken, newRefreshToken);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

}
