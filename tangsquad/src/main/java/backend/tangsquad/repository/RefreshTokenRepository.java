package backend.tangsquad.repository;

import backend.tangsquad.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofDays(7));
        redisTemplate.opsForValue().set(refreshToken, username, Duration.ofDays(7)); // refreshToken도 키로 저장
    }

    public Optional<RefreshToken> findByUsername(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(username))
                .map(refreshToken -> new RefreshToken(username, refreshToken));
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(refreshToken))
                .map(username -> new RefreshToken(username, refreshToken));
    }

    public void deleteByUsername(String username) {
        redisTemplate.delete(username);
        findByUsername(username).ifPresent(token -> redisTemplate.delete(token.getRefreshToken())); // refreshToken도 삭제
    }

    public void deleteByRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
        findByRefreshToken(refreshToken).ifPresent(token -> redisTemplate.delete(token.getUsername())); // username도 삭제
    }

}
