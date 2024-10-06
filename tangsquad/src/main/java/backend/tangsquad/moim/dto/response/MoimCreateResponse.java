package backend.tangsquad.moim.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MoimCreateResponse {
    private Long id;
    private Long userId;
    private Boolean anonymous;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;
    private String locationOne;
    private String locationTwo;
    private String locationThree;
    private String age;
    private String moodOne;
    private String moodTwo;
}
