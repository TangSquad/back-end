package backend.tangsquad.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class VerificationService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public VerificationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String ATTEMPT_KEY_SUFFIX = ":attempts";

    public void saveVerificationCode(String email, String code) {
        System.out.println("Received email: " + email);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, code, 10, TimeUnit.MINUTES);
        ops.set(email + ATTEMPT_KEY_SUFFIX, "0", 10, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedCode = ops.get(email);

        return storedCode != null && storedCode.equals(code);
    }

    public boolean canResendCode(String email) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attempts = ops.get(email + ATTEMPT_KEY_SUFFIX);

        return attempts == null || Integer.parseInt(attempts) < 3;
    }

    public void incrementResendAttempts(String email) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.increment(email + ATTEMPT_KEY_SUFFIX);
    }

    public String generateCode() {
        return String.valueOf((int)((Math.random() * 900000) + 100000));
    }

    public void deleteCode(String email) {
        redisTemplate.delete(email);
        redisTemplate.delete(email + ATTEMPT_KEY_SUFFIX);
    }
}
