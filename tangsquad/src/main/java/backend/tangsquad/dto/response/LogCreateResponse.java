package backend.tangsquad.dto.response;

import backend.tangsquad.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogCreateResponse {
    private User user;
    private Long id;
}
