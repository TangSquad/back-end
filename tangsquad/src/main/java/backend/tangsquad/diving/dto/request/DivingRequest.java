package backend.tangsquad.diving.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DivingRequest {
    private String divingName;
    private Long userId;
    private String divingIntro;
    private String thumbnailUrl;
    private Long currentPeople;
    private Boolean isPublic;
    private String age;
    private List<String> moods;
    private Long limitPeople;
    private String licenseLimit;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;

    @Builder
    public DivingRequest(String divingName, Long userId, String divingIntro, String thumbnailUrl, Long currentPeople, Boolean isPublic,String age, List<String> moods, Long limitPeople, String licenseLimit, LocalDate startDate, LocalDate endDate, String location) {
        this.divingName = divingName;
        this.userId = userId;
        this.divingIntro = divingIntro;
        this.thumbnailUrl = thumbnailUrl;
        this.currentPeople = currentPeople;
        this.isPublic = isPublic;
        this.age = age;
        this.moods = moods;
        this.limitPeople = limitPeople;
        this.licenseLimit = licenseLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
}
