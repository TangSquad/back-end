package backend.tangsquad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneRequest {
    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(\\d{3}|\\d{4})-(\\d{4})$", message = "Invalid phone number format.")
    private String phoneNumber;
}
