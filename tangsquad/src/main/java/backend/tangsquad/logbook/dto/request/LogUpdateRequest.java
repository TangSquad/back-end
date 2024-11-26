package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.logbook.entity.Whether;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LogUpdateRequest {
    private Long logId;
    private String viewSight;
    private String tide;
    private String startDiveTime;
    private String endDiveTime;
    private String timeDiffDive;
    private Long avgDepDiff;
    private Long maxDiff;
    private Long startBar;
    private Long endBar;
    private Long diffBar;
    private Whether whether;

    @Builder
    public LogUpdateRequest(Long logId, String viewSight, String tide, String startDiveTime, String endDiveTime,
                            String timeDiffDive, Long avgDepDiff, Long maxDiff, Long startBar,
                            Long endBar, Long diffBar, Whether whether) {
        this.logId = logId;
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
        this.whether = whether;
    }
}
