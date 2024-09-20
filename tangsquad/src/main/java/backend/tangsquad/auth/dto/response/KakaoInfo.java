package backend.tangsquad.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class KakaoInfo {
    private String name;
    private String email;
    private String phone_number;
}
