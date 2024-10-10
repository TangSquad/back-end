package backend.tangsquad.moim.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.moim.dto.request.MoimLeaderRequest;
import backend.tangsquad.moim.dto.request.MoimLeaderUsernameRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name="moim")
public class Moim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // Ensure nullable=false matches DB constraint
    @JsonManagedReference
    // 모임의 소유자
    private User user;

    @Column
    private Boolean anonymous;

    @Column
    private String moimName;

    @Column
    private String moimIntro;

    @Column
    private String moimDetails;

    @Column
    private Long limitPeople;

    @Column
    private Long expense;

    @Column
    private String licenseLimit;

    // Basic type
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "locations")
    private List<String> locations = new ArrayList<String>();

    @Column
    private String age;

    // Basic type
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "moods")
    private List<String> moods = new ArrayList<String>();

    @ManyToMany
    @JoinTable(
            name = "moim_user",
            joinColumns = @JoinColumn(name = "moim_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private List<User> registeredUsers; // List of registered users

    public void update(MoimUpdateRequest moimUpdateRequest) {
        if (moimUpdateRequest.getAnonymous() != null) this.anonymous = moimUpdateRequest.getAnonymous();
        if (moimUpdateRequest.getMoimName() != null) this.moimName = moimUpdateRequest.getMoimName();
        if (moimUpdateRequest.getMoimIntro() != null) this.moimIntro = moimUpdateRequest.getMoimIntro();
        if (moimUpdateRequest.getMoimDetails() != null) this.moimDetails = moimUpdateRequest.getMoimDetails();
        if (moimUpdateRequest.getLimitPeople() != null) this.limitPeople = moimUpdateRequest.getLimitPeople();
        if (moimUpdateRequest.getExpense() != null) this.expense = moimUpdateRequest.getExpense();
        if (moimUpdateRequest.getLicenseLimit() != null) this.licenseLimit = moimUpdateRequest.getLicenseLimit();
        if (moimUpdateRequest.getLocations() != null) this.locations = moimUpdateRequest.getLocations();
        if (moimUpdateRequest.getAge() != null) this.age = moimUpdateRequest.getAge();
        if (moimUpdateRequest.getMoods() != null) this.moods = moimUpdateRequest.getMoods();
        if (moimUpdateRequest.getRegisteredUsers() != null) this.registeredUsers = moimUpdateRequest.getRegisteredUsers();
    }

    public void update(MoimLeaderRequest moimLeaderRequest) {
        if (moimLeaderRequest.getUserId() != null) moimLeaderRequest.getUserId();
    }

    public void update(MoimLeaderUsernameRequest moimUserLeaderRequest) {
        if (moimUserLeaderRequest.getNewUsername() != null) this.getUser() = ;
    }



    @Builder
    public Moim(User user, Boolean anonymous, String moimName, String moimIntro, String moimDetails, Long limitPeople, Long expense, String licenseLimit, List<String> locations, String age, List<String> moods, List<User> registeredUsers) {
        this.user = user;
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
