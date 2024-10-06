package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogCreateResponse {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private String title;

    private Long squadId;

    private String contents;

    private String location;

    private Long weather;

    private Float surfTemp;

    private Float underTemp;

    private String viewSight;

    private String tide;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDiveTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDiveTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timeDiffDive;

    private Float avgDepDiff;

    private Float maxDiff;

    private Long startBar;

    private Long endBar;

    private Long diffBar;
}
