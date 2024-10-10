package backend.tangsquad.moim.dto.response;


import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MoimReadResponse {
    private Long moimId;
    private Long userId;
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


    @Builder
    public MoimReadResponse(Long moimId, Long userId, Boolean anonymous, String moimName, String moimIntro, String moimDetails, Long limitPeople, Long expense, String licenseLimit, List<String> locations, String age, List<String> moods, List<User> registeredUsers) {
        this.moimId = moimId;
        this.userId = userId;
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
        this.registeredUsers = registeredUsers;
    }
}
