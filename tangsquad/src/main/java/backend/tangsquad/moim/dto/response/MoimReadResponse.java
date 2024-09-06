package backend.tangsquad.moim.dto.response;


import backend.tangsquad.domain.User;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MoimReadResponse {
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
    private Long age;
    private String moodOne;
    private String moodTwo;
}
