package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Integer id;

    // 키
    @Column private String height;
    // 몸무게
    @Column private String weight;

    //수트
    @Column private String suit;

    //무게추
    @Column private String weightBelt;

    //BC
    @Column private String bc;

    //슈즈
    @Column private String shoes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile userProfile;

}
