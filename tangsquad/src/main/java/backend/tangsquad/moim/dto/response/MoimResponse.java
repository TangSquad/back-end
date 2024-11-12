package backend.tangsquad.moim.dto.response;

import backend.tangsquad.common.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MoimResponse {
    private Long id;
    private Long userId;
    private String thumbnailurl;
    private Boolean isPublic;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long currentPeople;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;
    private List<String> locations;
    private List<String> registeredUserIds;
    private String age;

    @Builder
    public MoimResponse(Long id, Long userId, String thumbnailurl, Boolean isPublic, String moimName, String moimIntro, String moimDetails, Long currentPeople, Long limitPeople, Long expense, String licenseLimit, List<String> locations, List<String> registeredUserIds, String age, List<String> moods) {
        this.id = id;
        this.userId = userId;
        this.thumbnailurl = thumbnailurl;
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
    }

    private List<String> moods;

}
