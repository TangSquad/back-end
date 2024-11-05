package backend.tangsquad.diving.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DivingResponse {
    private String divingName;
    private String divingIntro;
    private Boolean isPublic;
    private String age;
    private List<String> moods;
    private Long limitPeople;
    private String limitLicense;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;

    @Builder
    public DivingResponse(String divingName, String divingIntro, Boolean isPublic, String age, List<String> moods, Long limitPeople, String limitLicense, LocalDate startDate, LocalDate endDate, String location) {
        this.divingName = divingName;
        this.divingIntro = divingIntro;
        this.isPublic = isPublic;
        this.age = age;
        this.moods = moods;
        this.limitPeople = limitPeople;
        this.limitLicense = limitLicense;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
}
