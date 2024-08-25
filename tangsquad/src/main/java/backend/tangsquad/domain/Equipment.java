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
    @Column private Long id;

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

    //마스크
    @Column private String mask;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id", nullable = false)
    private UserProfile userProfile;

    @PrePersist
    @PreUpdate
    private void setDefault() {
        if (this.height == null || this.height.isEmpty()) {
            this.height = "";
        }
        if (this.weight == null || this.weight.isEmpty()) {
            this.weight = "";
        }
        if (this.suit == null || this.suit.isEmpty()) {
            this.suit = "";
        }
        if (this.weightBelt == null || this.weightBelt.isEmpty()) {
            this.weightBelt = "";
        }
        if (this.bc == null || this.bc.isEmpty()) {
            this.bc = "";
        }
        if (this.shoes == null || this.shoes.isEmpty()) {
            this.shoes = "";
        }
    }

}
