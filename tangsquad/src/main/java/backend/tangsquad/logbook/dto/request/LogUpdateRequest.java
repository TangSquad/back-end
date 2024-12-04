package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.diving.entity.Location;
import backend.tangsquad.logbook.entity.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class LogUpdateRequest {

    private Long logbookId;
    private Long logId;
    private List<Location> locations;
    private Whether whether;
    private Double airTemp;
    private Double surfTemp;
    private Double bottTemp;
    private String viewSight;
    private Level tide;
    private Level wave;
    private Level surge;
    private String diveTime;
    private Subject subject;
    private Long avgDepth;
    private Long maxDepth;
    private Long startBar;
    private Long endBar;

    @Builder
    public LogUpdateRequest(Long logbookId, Long logId, List<Location> locations, Whether whether, Double airTemp, Double surfTemp, Double bottTemp, String viewSight, Level tide, Level wave, Level surge, String diveTime, Subject subject, Long avgDepth, Long maxDepth, Long startBar, Long endBar) {
        this.logbookId = logbookId;
        this.logId = logId;
        this.locations = locations;
        this.whether = whether;
        this.airTemp = airTemp;
        this.surfTemp = surfTemp;
        this.bottTemp = bottTemp;
        this.viewSight = viewSight;
        this.tide = tide;
        this.wave = wave;
        this.surge = surge;
        this.diveTime = diveTime;
        this.subject = subject;
        this.avgDepth = avgDepth;
        this.maxDepth = maxDepth;
        this.startBar = startBar;
        this.endBar = endBar;
    }
}
