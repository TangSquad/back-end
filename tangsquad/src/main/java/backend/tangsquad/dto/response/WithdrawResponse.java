package backend.tangsquad.dto.response;

import lombok.Getter;

@Getter
public class WithdrawResponse {
    private boolean success;
    private String message;

    public WithdrawResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
