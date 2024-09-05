package backend.tangsquad.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="moim")
public class Moim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
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

}
