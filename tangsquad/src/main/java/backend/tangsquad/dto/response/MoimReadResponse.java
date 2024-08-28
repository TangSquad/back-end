package backend.tangsquad.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MoimReadResponse {
    private Long id;

    private String moimOwner;

    //    private String[] moimMembers;
    private String moimMembers;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String moimName;
    private String moimIntro;
    private String moimContents;
    private Integer maxPeople;
    private Float price;

    //    private String[] licenseLimit;
    private String licenseLimit;
    //    private String[] region;
    private String region;
    private String age;

    //    private String[] mood;
    private String mood;
}
