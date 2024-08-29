package backend.tangsquad.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "추가 정보 입력")
public class AdditionalInfoRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    String nickname;
    Long organizationId;
    Long levelId;
    String certificateImage;
}
