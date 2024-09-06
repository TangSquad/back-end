package backend.tangsquad.common.service;

import backend.tangsquad.util.SmsUtil;
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
    private final SmsUtil smsUtil;

    @Autowired
    public VerificationService(RedisTemplate<String, String> redisTemplate, SmsUtil smsUtil) {
        this.redisTemplate = redisTemplate;
        this.smsUtil = smsUtil;
    }

    private static final String ATTEMPT_KEY_SUFFIX = ":attempts";
    private static final int CODE_EXPIRATION_MINUTES = 10;

    // 이메일 인증 코드 저장
    public void saveEmailVerificationCode(String email, String code) {
        System.out.println("Received email: " + email);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, code, 10, TimeUnit.MINUTES);
        ops.set(email + ATTEMPT_KEY_SUFFIX, "0", 10, TimeUnit.MINUTES);
    }

    // 폰 인증 코드 저장
    public void savePhoneVerificationCode(String phoneNumber, String code) {
        System.out.println("Received phone number: " + phoneNumber);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(phoneNumber, code, 10, TimeUnit.MINUTES);
        ops.set(phoneNumber + ATTEMPT_KEY_SUFFIX, "0", 10, TimeUnit.MINUTES);
    }

    // 이메일 코드 검증
    public boolean verifyEmailCode(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedCode = ops.get(email);

        return storedCode != null && storedCode.equals(code);
    }

    // 폰 코드 검증
    public boolean verifyPhoneCode(String phoneNumber, String code) {
        String sanitizedPhoneNumber = phoneNumber.replaceAll("-", "");
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedCode = ops.get(sanitizedPhoneNumber);

        // redis에 등록된 전화번호이면서, 코드가 000000이 입력된 경우 프리패스
        if (storedCode != null && code.equals("000000")) {
            return true;
        }

        return storedCode != null && storedCode.equals(code);
    }


    // 이메일 코드 재전송 가능 여부 확인
    public boolean canResendEmailCode(String email) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attempts = ops.get(email + ATTEMPT_KEY_SUFFIX);

        return attempts == null || Integer.parseInt(attempts) < 3;
    }

    // 폰 코드 재전송 가능 여부 확인
    public boolean canResendPhoneCode(String phoneNumber) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attempts = ops.get(phoneNumber + ATTEMPT_KEY_SUFFIX);

        return attempts == null || Integer.parseInt(attempts) < 3;
    }

    // 이메일 코드 재전송 시도 횟수 증가
    public void incrementResendEmailAttempts(String email) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.increment(email + ATTEMPT_KEY_SUFFIX);
    }

    // 폰 코드 재전송 시도 횟수 증가
    public void incrementResendPhoneAttempts(String phoneNumber) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.increment(phoneNumber + ATTEMPT_KEY_SUFFIX);
    }

    // 인증 코드 생성
    public String generateCode() {
        return String.valueOf((int)((Math.random() * 900000) + 100000));
    }

    // 이메일 코드 삭제
    public void deleteEmailCode(String email) {
        redisTemplate.delete(email);
        redisTemplate.delete(email + ATTEMPT_KEY_SUFFIX);
    }

    // 폰 코드 삭제
    public void deletePhoneCode(String phoneNumber) {
        String sanitizedPhoneNumber = phoneNumber.replaceAll("-", "");
        redisTemplate.delete(sanitizedPhoneNumber);
        redisTemplate.delete(sanitizedPhoneNumber + ATTEMPT_KEY_SUFFIX);
    }

    public void sendPhoneVerificationCode(String phoneNumber) {
        String sanitizedPhoneNumber = phoneNumber.replaceAll("-", "");
        String code = generateCode();

        // Redis에 인증 코드 저장
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(sanitizedPhoneNumber, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        ops.set(sanitizedPhoneNumber + ATTEMPT_KEY_SUFFIX, "0", CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        // SMS 전송
        // smsUtil.sendSms(sanitizedPhoneNumber, code);
    }
}
