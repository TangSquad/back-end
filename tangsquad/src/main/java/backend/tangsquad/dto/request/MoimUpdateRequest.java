package backend.tangsquad.dto.request;


import backend.tangsquad.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoimUpdateRequest {
    private Long id;
//    private User user;
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
