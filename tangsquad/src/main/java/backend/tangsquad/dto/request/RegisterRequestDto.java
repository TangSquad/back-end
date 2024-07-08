package backend.tangsquad.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @Email
    private String email;

    private String password;

    private String username;

    private String name;

    @Pattern(regexp = "01[06789]-[0-9]{3,4}-[0-9]{4}")
    private String phone;

    private List<CertificationDto> certifications;

}
