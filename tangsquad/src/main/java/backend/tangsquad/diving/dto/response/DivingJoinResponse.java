package backend.tangsquad.diving.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class DivingJoinResponse {
    private Long divingId;
    private List<String> registeredUserIds;
}
