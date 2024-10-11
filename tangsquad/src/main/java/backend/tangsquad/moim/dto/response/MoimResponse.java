package backend.tangsquad.moim.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MoimResponse {
    private Long userId;
    private Boolean anonymous;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;

    @Builder
    public MoimResponse(Long userId, Boolean anonymous, String moimName, String moimIntro, String moimDetails, Long limitPeople, Long expense, String licenseLimit) {
        this.userId = userId;
        this.anonymous = anonymous;
        this.moimName = moimName;
        this.moimIntro = moimIntro;
        this.moimDetails = moimDetails;
        this.limitPeople = limitPeople;
        this.expense = expense;
        this.licenseLimit = licenseLimit;
    }
}
