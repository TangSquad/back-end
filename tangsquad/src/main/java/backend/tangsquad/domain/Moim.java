package backend.tangsquad.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="moim")
public class Moim {

    @Id
    private Long id;

    @Column
    private String moimOwner;

//    private String[] moimMembers;
    @Column
    private String moimMembers;
    @Column
    private LocalDateTime startDate;
    @Column
    private LocalDateTime endDate;
    @Column
    private String moimName;
    @Column
    private String moimIntro;
    @Column
    private String moimContents;
    @Column
    private Integer maxPeople;
    @Column
    private Float price;

//    private String[] licenseLimit;
    @Column
    private String licenseLimit;
//    private String[] region;
    @Column
    private String region;
    @Column
    private String age;

//    private String[] mood;
    @Column
    private String mood;

}
