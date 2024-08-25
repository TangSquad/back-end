package backend.tangsquad.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NicknameCheckRequest {
    @NotBlank(message = "Nickname is required.")
    private String nickname;
}
