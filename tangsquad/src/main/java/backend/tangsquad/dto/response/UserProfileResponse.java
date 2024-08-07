package backend.tangsquad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String profileImageUrl;
    private String name;
    private String username;
    private List<String> certifications;
    private Integer clubCount;
    private Integer divingCount;
    private Integer logBookCount;
}
