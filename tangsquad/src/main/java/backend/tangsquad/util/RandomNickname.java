package backend.tangsquad.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;

@Component
@AllArgsConstructor
public class RandomNickname {

    private final String[] ADJECTIVES = {
            "멋쟁이", "귀여운", "깜찍한", "똑똑한", "행복한", "빛나는", "용감한", "신나는", "든든한", "씩씩한", "즐거운"
    };

    private final String[] NOUNS = {
            "사자", "개구리", "토끼", "호랑이", "코끼리", "판다", "고양이", "강아지", "햄스터", "거북이", "블루탱", "레드탱", "돌고래", "상어"
    };

    private final Random RANDOM = new Random();

    public String generate() {
        // 형용사 + 명사 + 1~100 사이의 랜덤 숫자
        return MessageFormat.format("{0} {1} {2}호", ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)], NOUNS[RANDOM.nextInt(NOUNS.length)], RANDOM.nextInt(100) + 1);
    }
}
