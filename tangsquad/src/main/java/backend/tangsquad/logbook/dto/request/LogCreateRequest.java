package backend.tangsquad.logbook.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LogCreateRequest {
    private String viewSight;
    private String tide;
    private LocalTime startDiveTime;
    private LocalTime endDiveTime;
    private LocalTime timeDiffDive;
    private Long avgDepDiff;
    private Long maxDiff;
    private Long startBar;
    private Long endBar;
    private Long diffBar;
    private Long logbookId;

    @Builder
    public LogCreateRequest(String viewSight, String tide, LocalTime startDiveTime, LocalTime endDiveTime,
                            LocalTime timeDiffDive, Long avgDepDiff, Long maxDiff, Long startBar,
                            Long endBar, Long diffBar, Long logbookId) {
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
