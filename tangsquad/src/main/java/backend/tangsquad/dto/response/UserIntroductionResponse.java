package backend.tangsquad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIntroductionResponse {
    private String introduction;
    private List<String> certifications;
    private String link;
    private String affiliation;
    private String prevDiving;
}
