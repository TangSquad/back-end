package backend.tangsquad.moim.dto.request;


import backend.tangsquad.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoimUpdateRequest {
    private Long moimId;
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
    private List<User> registeredUsers; // List of registered users
}
