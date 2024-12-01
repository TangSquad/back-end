package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.logbook.entity.*;
import lombok.Builder;
import lombok.Data;

@Data
public class LogCreateRequest {
    private Whether whether;
    private Double airTemp;
    private Double surfTemp;
    private Double bottTemp;
    private String viewSight;
    private Level tide;
    private Level wave;
    private Level surge;
    private String startDiveTime;
    private String endDiveTime;
    private String diveTime;
    private Subject subject;
    private Long avgDepth;
    private Long maxDepth;
    private Long startBar;
    private Long endBar;
    private Long logbookId;

    @Builder
    public LogCreateRequest(Whether whether, Double airTemp, Double surfTemp, Double bottTemp, String viewSight, Level tide, Level wave, Level surge, String startDiveTime, String endDiveTime, String diveTime, Subject subject, Long avgDepth, Long maxDepth, Long startBar, Long endBar, Long logbookId) {
        this.whether = whether;
        this.airTemp = airTemp;
        this.surfTemp = surfTemp;
        this.bottTemp = bottTemp;
        this.viewSight = viewSight;
        this.tide = tide;
        this.wave = wave;
        this.surge = surge;
        this.startDiveTime = startDiveTime;
        this.endDiveTime = endDiveTime;
        this.diveTime = diveTime;
        this.subject = subject;
        this.avgDepth = avgDepth;
        this.maxDepth = maxDepth;
        this.startBar = startBar;
        this.endBar = endBar;
        this.logbookId = logbookId;
    }
}
