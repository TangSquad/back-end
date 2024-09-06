package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogCreateResponse {

    private Long id;

    private User user;

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
