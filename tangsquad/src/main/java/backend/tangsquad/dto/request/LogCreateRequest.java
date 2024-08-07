package backend.tangsquad.dto.request;

import backend.tangsquad.domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogCreateRequest {

    private User user;
    private Integer id;
    private LocalDateTime date;

    // 제목
    private String title;
    private Integer squadId;

    // 글
    private String contents;

    // 장소
    private String location;

    // 날씨
    private Integer weather;

    private Float surfTemp;
    private Float underTemp;
    private Character viewSight;
    private Character tide;

    // 시간
    private LocalDateTime startDiveTime;
    private LocalDateTime endDiveTime;

    private LocalDateTime timeDiffDive;

    // 수심 및 공기
    private Float AvgDepDiff;
    private Float maxDiff;

    private Integer startBar;
    private Integer endBar;
    private Integer diffBar;
}