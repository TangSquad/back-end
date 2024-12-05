package backend.tangsquad.diving.dto.request;

import backend.tangsquad.diving.entity.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class DivingRequest {
    private String divingName;
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
    private Location location;

    @Builder
    public DivingRequest(String divingName, String divingIntro, String thumbnailUrl, Long currentPeople, Boolean isPublic,String age, List<String> moods, Long limitPeople, String licenseLimit, LocalDate startDate, LocalDate endDate, Location location) {
        this.divingName = divingName;
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
