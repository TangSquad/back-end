package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.logbook.entity.Whether;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LogResponse {
    private Long id;
    private Long userId;
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
    private Long logbookId;
    private Whether whether;

    @Builder
    public LogResponse(Long id, Long userId, String viewSight, String tide, String startDiveTime,
                       String endDiveTime, String timeDiffDive, Long avgDepDiff, Long maxDiff,
                       Long startBar, Long endBar, Long diffBar, Long logbookId, Whether whether) {
        this.id = id;
        this.userId = userId;
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
        this.whether = whether;
    }
}
