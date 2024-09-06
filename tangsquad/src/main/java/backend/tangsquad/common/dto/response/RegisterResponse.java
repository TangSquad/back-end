package backend.tangsquad.common.dto.response;

import lombok.Getter;

@Getter
public class RegisterResponse {
    private boolean success;
    private String message;
    private Long userId;

    public RegisterResponse(boolean success, String message, Long userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }
}
