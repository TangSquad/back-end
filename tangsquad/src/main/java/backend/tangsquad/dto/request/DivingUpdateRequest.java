package backend.tangsquad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DivingUpdateRequest {
    private Long divingId;
    private Long userId;  // Changed from User to Long userId
    private String divingName;
    private String divingIntro;
    private Long age;
    private String moodOne;
    private String moodTwo;
    private Long limitPeople;
    private String limitLicense;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
}
