package backend.tangsquad.dto.request;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoimCreateRequest {

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
