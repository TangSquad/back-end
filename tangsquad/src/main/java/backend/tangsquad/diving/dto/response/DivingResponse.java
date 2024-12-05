package backend.tangsquad.diving.dto.response;

import backend.tangsquad.diving.entity.Location;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class DivingResponse {
    private Long id;
    private Long userId;
    private Boolean isPublic;
    private String thumbnailUrl;
    private String divingName;
    private String divingIntro;
    private String age;
    private List<String> moods;
    private Long currentPeople;
    private Long limitPeople;
    private String licenseLimit;
    private LocalDate startDate;
    private LocalDate endDate;
    private Location location;
    private List<Long> registeredUserIds;
    private UUID chatRoomId;

    @Builder
    public DivingResponse(Long id, Long userId, String divingName, String divingIntro, String thumbnailUrl, Long currentPeople, Boolean isPublic, String age, List<String> moods, Long limitPeople, String licenseLimit, LocalDate startDate, LocalDate endDate, Location location, List<Long> registeredUserIds, UUID chatRoomId) {
        this.id = id;
        this.userId = userId;
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
        this.registeredUserIds = registeredUserIds;
        this.chatRoomId = chatRoomId;
    }
}
