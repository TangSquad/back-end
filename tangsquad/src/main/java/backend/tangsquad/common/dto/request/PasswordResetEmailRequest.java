package backend.tangsquad.common.dto.request;

import lombok.Data;

@Data
public class PasswordResetEmailRequest {
    private String email;
}
