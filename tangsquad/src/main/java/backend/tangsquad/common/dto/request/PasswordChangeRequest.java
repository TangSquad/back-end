package backend.tangsquad.common.dto.request;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String newPassword;
}
