package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalTime startDiveTime;

    @Column
    private LocalTime endDiveTime;

    @Column
    private LocalTime timeDiffDive;

    @Column
    private Long avgDepDiff;

    @Column
    private Long maxDiff;

    @Column
    private Long startBar;

    @Column
    private Long endBar;

    @Column
    private Long diffBar;

//    @ManyToOne
//    @JoinColumn(name = "logbook_id", nullable = false)
//    private Logbook logbook;
    @Column
    private Long logbookId;

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
        if (logRequest.getLogbookId() != null) this.logbookId = logRequest.getLogbookId();

//        if (logRequest.getLogbook() != null) this.logbook = logRequest.getLogbook();
    }

    @Builder
    public Log(User user, String viewSight, String tide, LocalTime startDiveTime, LocalTime endDiveTime, LocalTime timeDiffDive, Long avgDepDiff, Long maxDiff, Long startBar, Long endBar, Long diffBar, Long logbookId) {
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
        this.logbookId = logbookId;
//        this.logbook = logbook;
    }
}
