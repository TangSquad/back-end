package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.request.LogRequest;
import backend.tangsquad.logbook.dto.request.LogUpdateRequest;
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

    public void update(LogUpdateRequest logUpdateRequest) {
        if (logUpdateRequest.getViewSight() != null) this.viewSight = logUpdateRequest.getViewSight();
        if (logUpdateRequest.getTide() != null) this.tide = logUpdateRequest.getTide();
        if (logUpdateRequest.getStartDiveTime() != null) this.startDiveTime = logUpdateRequest.getStartDiveTime();
        if (logUpdateRequest.getEndDiveTime() != null) this.endDiveTime = logUpdateRequest.getEndDiveTime();
        if (logUpdateRequest.getTimeDiffDive() != null) this.timeDiffDive = logUpdateRequest.getTimeDiffDive();
        if (logUpdateRequest.getAvgDepDiff() != null) this.avgDepDiff = logUpdateRequest.getAvgDepDiff();
        if (logUpdateRequest.getMaxDiff() != null) this.maxDiff = logUpdateRequest.getMaxDiff();
        if (logUpdateRequest.getStartBar() != null) this.startBar = logUpdateRequest.getStartBar();
        if (logUpdateRequest.getEndBar() != null) this.endBar = logUpdateRequest.getEndBar();
        if (logUpdateRequest.getDiffBar() != null) this.diffBar = logUpdateRequest.getDiffBar();
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
    }
}
