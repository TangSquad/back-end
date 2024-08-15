package backend.tangsquad.dto.request;

import lombok.Data;

@Data
public class RegisterCheckRequest {
    String email;
    String username;
    String phone;
}
