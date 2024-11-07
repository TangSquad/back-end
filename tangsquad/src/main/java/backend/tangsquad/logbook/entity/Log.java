package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "log")
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

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

    @ManyToOne
    @JoinColumn(name = "logbook_id", referencedColumnName = "id")  // assuming the column is named logbook_id
    private Logbook logbook;

    public void update(LogRequest logRequest) {
        if (logRequest.getViewSight() != null) this.viewSight = logRequest.getViewSight();
        if (logRequest.getTide() != null) this.tide = logRequest.getTide();
        if (logRequest.getStartDiveTime() != null) this.startDiveTime = logRequest.getStartDiveTime();
        if (logRequest.getEndDiveTime() != null) this.endDiveTime = logRequest.getEndDiveTime();
        if (logRequest.getTimeDiffDive() != null) this.timeDiffDive = logRequest.getTimeDiffDive();
        if (logRequest.getAvgDepDiff() != null) this.avgDepDiff = logRequest.getAvgDepDiff();
        if (logRequest.getMaxDiff() != null) this.maxDiff = logRequest.getMaxDiff();
        if (logRequest.getStartBar() != null) this.startBar = logRequest.getStartBar();
        if (logRequest.getEndBar() != null) this.endBar = logRequest.getEndBar();
        if (logRequest.getDiffBar() != null) this.diffBar = logRequest.getDiffBar();
        if (logRequest.getLogbook() != null) this.logbook = logRequest.getLogbook();
    }

    @Builder
    public Log(Long id, User user, String viewSight, String tide, LocalDateTime startDiveTime, LocalDateTime endDiveTime, LocalDateTime timeDiffDive, Float avgDepDiff, Float maxDiff, Long startBar, Long endBar, Long diffBar, Logbook logbook) {
        this.id = id;
        this.user = user;
        this.viewSight = viewSight;
        this.tide = tide;
        this.startDiveTime = startDiveTime;
        this.endDiveTime = endDiveTime;
        this.timeDiffDive = timeDiffDive;
        this.avgDepDiff = avgDepDiff;
        this.maxDiff = maxDiff;
        this.startBar = startBar;
        this.endBar = endBar;
        this.diffBar = diffBar;
        this.logbook = logbook;
    }
}
