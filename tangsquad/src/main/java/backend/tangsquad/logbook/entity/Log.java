<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> c31f0a1 (fix: Log 엔티티 생성, 불변의 정보는 Logbook에, 변하는 정보는 Log에 저장)
package backend.tangsquad.logbook.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
=======

>>>>>>> c31f0a1 (fix: Log 엔티티 생성, 불변의 정보는 Logbook에, 변하는 정보는 Log에 저장)
    @Column
    private String viewSight;

    @Column
    private String tide;

    @Column
    private LocalDateTime startDiveTime;

    @Column
    private LocalDateTime endDiveTime;

    @Column
    private LocalDateTime timeDiffDive;

    @Column
    private Float avgDepDiff;

    @Column
    private Float maxDiff;

    @Column
    private Long startBar;

    @Column
    private Long endBar;

    @Column
    private Long diffBar;

<<<<<<< HEAD
=======
package backend.tangsquad.logbook.entity;public class Log {
>>>>>>> 08e0c42 (refactor: Logbook CREATE 기능 refactor)
=======
>>>>>>> c31f0a1 (fix: Log 엔티티 생성, 불변의 정보는 Logbook에, 변하는 정보는 Log에 저장)
}
