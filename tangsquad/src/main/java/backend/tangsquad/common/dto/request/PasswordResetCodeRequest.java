package backend.tangsquad.common.dto.request;

import lombok.Data;

@Data
public class PasswordResetCodeRequest {
    private String email;
    private String code;
}
