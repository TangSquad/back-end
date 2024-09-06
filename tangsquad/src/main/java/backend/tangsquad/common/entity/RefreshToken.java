package backend.tangsquad.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*7)
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String nickname;
    @Indexed
    private String refreshToken;
}
