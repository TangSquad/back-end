package backend.tangsquad.moim.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MoimResponse {
    private Long userId;
    private Boolean isPublic;
    private String thumbnailurl;
    private Long currentPeople;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;

    @Builder
    public MoimResponse(Long userId, Boolean isPublic, String moimName, String moimIntro, Long currentPeople, String moimDetails, Long limitPeople, Long expense, String licenseLimit) {
        this.userId = userId;
        this.isPublic = isPublic;
        this.moimName = moimName;
        this.currentPeople = currentPeople;
        this.moimIntro = moimIntro;
        this.moimDetails = moimDetails;
        this.limitPeople = limitPeople;
        this.expense = expense;
        this.licenseLimit = licenseLimit;
    }
}
