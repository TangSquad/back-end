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

    public void save(String nickname, String refreshToken) {
        redisTemplate.opsForValue().set(nickname, refreshToken, Duration.ofDays(7));
        redisTemplate.opsForValue().set(refreshToken, nickname, Duration.ofDays(7)); // refreshToken도 키로 저장
    }

    public Optional<RefreshToken> findByNickname(String nickname) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(nickname))
                .map(refreshToken -> new RefreshToken(nickname, refreshToken));
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(refreshToken))
                .map(nickname -> new RefreshToken(nickname, refreshToken));
    }

    public void deleteByNickname(String nickname) {
        redisTemplate.delete(nickname);
        findByNickname(nickname).ifPresent(token -> redisTemplate.delete(token.getRefreshToken())); // refreshToken도 삭제
    }

    public void deleteByRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
        findByRefreshToken(refreshToken).ifPresent(token -> redisTemplate.delete(token.getNickname())); // nickname도 삭제
    }

}
