package backend.tangsquad.moim.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoimCreateResponse {
    private Boolean isPublic;
    private String thumbnailurl;
    private String moimName;
    private String moimIntro;
    private String moimDetails;
    private Long limitPeople;
    private Long expense;
    private String licenseLimit;
    private List<String> locations = new ArrayList<String>();
    private String age;
    private List<String> moods = new ArrayList<String>();
}
