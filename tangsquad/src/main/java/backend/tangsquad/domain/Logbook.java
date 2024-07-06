package backend.tangsquad.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "logbook")
public class Logbook {

    // 다이빙 로그, 날짜
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Integer id;
    @Column private LocalDateTime date;


    // 제목
    @Column private String title;

    @Column private Integer squadId;

    // 글
    @Column private String contents;

    // 장소
    @Column private String location;

    // 날씨
    @Column private Integer weather;

    @Column private Float surfTemp;
    @Column private Float underTemp;
    @Column private Character viewSight;
    @Column private Character tide;

    // 시간
    @Column private LocalDateTime startDiveTime;
    @Column private LocalDateTime endDiveTime;

    @Column private LocalDateTime timeDiffDive;

    // 수심 및 공기
    @Column private Float AvgDepDiff;
    @Column private Float maxDiff;

    @Column private Integer startBar;
    @Column private Integer endBar;
    @Column private Integer diffBar;

    // 참여자는, 유저 엔티티가 만들어진 후에 리스트 형태로 만들어야 할 듯.

    // 장비도 유저 엔티티가 만들어진 후에.
}
