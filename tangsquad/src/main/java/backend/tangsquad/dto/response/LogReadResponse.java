package backend.tangsquad.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
