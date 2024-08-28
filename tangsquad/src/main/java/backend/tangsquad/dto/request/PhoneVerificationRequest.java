package backend.tangsquad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhoneVerificationRequest {
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "전화번호는 010-1234-5678 형식으로 입력해야 합니다.")
    private String phoneNumber;
}
