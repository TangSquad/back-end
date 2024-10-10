package backend.tangsquad.moim.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class MoimCreateResponse {
    private Boolean anonymous;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;
    private List<String> locations = new ArrayList<String>();
    private String age;
    private List<String> moods = new ArrayList<String>();

    @Builder
    public MoimCreateResponse(Boolean anonymous, String moimName, String moimIntro, String moimDetails, Long limitPeople, Long expense, String licenseLimit, List<String> locations, String age, List<String> moods) {
        this.anonymous = anonymous;
        this.moimName = moimName;
        this.moimIntro = moimIntro;
        this.moimDetails = moimDetails;
        this.limitPeople = limitPeople;
        this.expense = expense;
        this.licenseLimit = licenseLimit;
        this.locations = locations;
        this.age = age;
        this.moods = moods;
    }

}
