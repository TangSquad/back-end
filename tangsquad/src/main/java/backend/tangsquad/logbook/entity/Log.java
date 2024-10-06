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

}
