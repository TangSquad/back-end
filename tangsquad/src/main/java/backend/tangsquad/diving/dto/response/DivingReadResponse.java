package backend.tangsquad.diving.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DivingReadResponse {
    private Long divingId;
    private Long userId;  // Changed from User to Long userId
    private String divingName;
    private String divingIntro;
    private String age;
    private String moodOne;
    private String moodTwo;
    private Long limitPeople;
    private String limitLicense;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
}
