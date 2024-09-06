package backend.tangsquad.common.dto.response;

import lombok.Getter;

@Getter
public class JwtResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final String type = "Bearer";

    public JwtResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
