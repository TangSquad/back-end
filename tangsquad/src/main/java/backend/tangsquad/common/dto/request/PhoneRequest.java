package backend.tangsquad.common.dto.request;

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
    @Pattern(regexp = "^01\\d{8,9}$", message = "Invalid phone number. It should start with '01' and be 10 or 11 digits long.")
    private String phoneNumber;
}
