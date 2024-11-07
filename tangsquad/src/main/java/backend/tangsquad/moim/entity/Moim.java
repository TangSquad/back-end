package backend.tangsquad.moim.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
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
    private String thumbnailUrl;

    @Column
    private Long currentPeople;

    @Column
    private Boolean isPublic;

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

    @OneToMany
    private List<Logbook> logbooks = new ArrayList<>();

    public void update(MoimUpdateRequest moimUpdateRequest) {
        if (moimUpdateRequest.getIsPublic() != null) this.isPublic = moimUpdateRequest.getIsPublic();
        if (moimUpdateRequest.getMoimName() != null) this.moimName = moimUpdateRequest.getMoimName();
        if (moimUpdateRequest.getMoimIntro() != null) this.moimIntro = moimUpdateRequest.getMoimIntro();
        if (moimUpdateRequest.getCurrentPeople() != null) this.currentPeople = moimUpdateRequest.getCurrentPeople();
        if (moimUpdateRequest.getMoimDetails() != null) this.moimDetails = moimUpdateRequest.getMoimDetails();
        if (moimUpdateRequest.getLimitPeople() != null) this.limitPeople = moimUpdateRequest.getLimitPeople();
        if (moimUpdateRequest.getThumbnailUrl() != null) this.thumbnailUrl = moimUpdateRequest.getThumbnailUrl();
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
        if (moimUserLeaderRequest.getUser() != null) this.user = moimUserLeaderRequest.getUser();
    }

    @Builder
    public Moim(User user, Boolean isPublic, String thumbnailUrl, Long currentPeople, String moimName, String moimIntro, String moimDetails, Long limitPeople, Long expense, String licenseLimit, List<String> locations, String age, List<String> moods, List<User> registeredUsers) {
        this.user = user;
        this.isPublic = isPublic;
        this.thumbnailUrl = thumbnailUrl;
        this.currentPeople = currentPeople;
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
