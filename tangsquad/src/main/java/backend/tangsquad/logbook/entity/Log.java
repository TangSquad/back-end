package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.diving.entity.Location;
import backend.tangsquad.logbook.dto.request.LogUpdateRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    // 장소
    @Column
    private List<Location> locations;

    // ---- 날씨
    // 날씨 - SUNNY, CLOUDY, ...
    @Column
    @Enumerated(EnumType.STRING)
    private Whether whether;


    // 기온
    @Column
    private Double airTemp;

    // 수면온도
    @Column
    private Double surfTemp;

    // 바닥온도
    @Column
    private Double bottTemp;

    // ---- 수중 환경
    // 시야
    @Column
    private String viewSight;

    // 조류
    @Column
    @Enumerated(EnumType.STRING)
    private Level tide;

    // 파도
    @Column
    @Enumerated(EnumType.STRING)
    private Level wave;

    // 서지
    @Column
    @Enumerated(EnumType.STRING)
    private Level surge;

    @Column
    private String diveTime;

    // 다이빙 주제
    @Column
    private Subject subject;


    @Column
    private Long avgDepth;

    @Column
    private Long maxDepth;

    @Column
    private Long startBar;

    @Column
    private Long endBar;

    @ManyToOne
    @JoinColumn(name = "logbook_id", nullable = false)
    private Logbook logbook;


    @Builder
    public Log(Long id, User user, List<Location> locations, Whether whether, Double airTemp, Double surfTemp, Double bottTemp, String viewSight, Level tide, Level wave, Level surge, String diveTime, Subject subject, Long avgDepth, Long maxDepth, Long startBar, Long endBar, Logbook logbook) {
        this.id = id;
        this.user = user;
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
        this.logbook = logbook;
    }

    public void update(LogUpdateRequest logUpdateRequest) {
        if (logUpdateRequest.getLocations() != null) this.locations = logUpdateRequest.getLocations();
        if (logUpdateRequest.getWhether() != null) this.whether = logUpdateRequest.getWhether();
        if (logUpdateRequest.getAirTemp() != null) this.airTemp = logUpdateRequest.getAirTemp();
        if (logUpdateRequest.getSurfTemp() != null) this.surfTemp = logUpdateRequest.getSurfTemp();
        if (logUpdateRequest.getBottTemp() != null) this.bottTemp = logUpdateRequest.getBottTemp();
        if (logUpdateRequest.getViewSight() != null) this.viewSight = logUpdateRequest.getViewSight();
        if (logUpdateRequest.getTide() != null) this.tide = logUpdateRequest.getTide();
        if (logUpdateRequest.getWave() != null) this.wave = logUpdateRequest.getWave();
        if (logUpdateRequest.getSurge() != null) this.surge = logUpdateRequest.getSurge();
        if (logUpdateRequest.getDiveTime() != null) this.diveTime = logUpdateRequest.getDiveTime();
        if (logUpdateRequest.getSubject() != null) this.subject = logUpdateRequest.getSubject();
        if (logUpdateRequest.getAvgDepth() != null) this.avgDepth = logUpdateRequest.getAvgDepth();
        if (logUpdateRequest.getMaxDepth() != null) this.maxDepth = logUpdateRequest.getMaxDepth();
        if (logUpdateRequest.getStartBar() != null) this.startBar = logUpdateRequest.getStartBar();
        if (logUpdateRequest.getEndBar() != null) this.endBar = logUpdateRequest.getEndBar();
    }

}
