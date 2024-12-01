package backend.tangsquad.moim.dto.response;

import backend.tangsquad.common.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MoimResponse {
    private Long id;
    private Long userId;
    private String thumbnailUrl;
    private Boolean isPublic;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long currentPeople;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;
    private List<String> locations;
    private List<Long> registeredUserIds;
    private String age;
    private List<String> moods;
    private UUID chatRoomId;

    @Builder
    public MoimResponse(Long id, Long userId, String thumbnailUrl, Boolean isPublic, String moimName, String moimIntro, String moimDetails, Long currentPeople, Long limitPeople, Long expense, String licenseLimit, List<String> locations, List<Long> registeredUserIds, String age, List<String> moods, UUID chatRoomId) {
        this.id = id;
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.moimName = moimName;
        this.moimIntro = moimIntro;
        this.moimDetails = moimDetails;
        this.currentPeople = currentPeople;
        this.limitPeople = limitPeople;
        this.expense = expense;
        this.licenseLimit = licenseLimit;
        this.locations = locations;
        this.registeredUserIds = registeredUserIds;
        this.age = age;
        this.moods = moods;
        this.chatRoomId = chatRoomId;
    }

}
