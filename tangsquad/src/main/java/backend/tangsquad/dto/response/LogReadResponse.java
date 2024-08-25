package backend.tangsquad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogReadResponse {
    private Long id;
    private Long userId; // Replace User with User ID or simpler representation
    private LocalDateTime date;
    private String title;
    private Integer squadId;
    private String contents;
    private String location;
    private Integer weather;
    private Float surfTemp;
    private Float underTemp;
    private Character viewSight;
    private Character tide;
    private LocalDateTime startDiveTime;
    private LocalDateTime endDiveTime;
    private LocalDateTime timeDiffDive;
    private Float avgDepDiff;
    private Float maxDiff;
    private Integer startBar;
    private Integer endBar;
    private Integer diffBar;
}
