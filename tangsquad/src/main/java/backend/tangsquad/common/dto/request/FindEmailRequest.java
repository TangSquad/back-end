package backend.tangsquad.common.dto.request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindEmailRequest {
    private String phoneNumber;
    private String code;
}
