package backend.tangsquad.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column
    private String locationOne;

    @Column
    private String locationTwo;

    @Column
    private String locationThree;

    @Column
    private Long age;

    @Column
    private String moodOne;

    @Column
    private String moodTwo;

    @ManyToMany
    @JoinTable(
            name = "moim_user",
            joinColumns = @JoinColumn(name = "moim_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private List<User> registeredUsers; // List of registered users


}
