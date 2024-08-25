package backend.tangsquad.domain;

import backend.tangsquad.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "logbook")
public class Logbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // In Logbook class
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column
    private Integer squadId;

    @Column
    private String contents;

    @Column
    private String location;

    @Column
    private Integer weather;

    @Column
    private Float surfTemp;

    @Column
    private Float underTemp;

    @Column
    private Character viewSight;

    @Column
    private Character tide;

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
    private Integer startBar;

    @Column
    private Integer endBar;

    @Column
    private Integer diffBar;
}
