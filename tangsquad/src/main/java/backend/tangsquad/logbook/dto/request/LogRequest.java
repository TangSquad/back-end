package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogRequest {
    private Long id;
    private String viewSight;
    private String tide;
    private LocalDateTime startDiveTime;
    private LocalDateTime endDiveTime;
    private LocalDateTime timeDiffDive;
    private Float avgDepDiff;
    private Float maxDiff;
    private Long startBar;
    private Long endBar;
    private Long diffBar;
    private Logbook logbook;

    @Builder
    public LogRequest(Long id, String viewSight, String tide, LocalDateTime startDiveTime, LocalDateTime endDiveTime, LocalDateTime timeDiffDive, Float avgDepDiff, Float maxDiff, Long startBar, Long endBar, Long diffBar, Logbook logbook) {
        this.id = id;
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
