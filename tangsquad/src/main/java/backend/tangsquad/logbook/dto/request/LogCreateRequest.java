package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogCreateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    // 제목
    private String title;

//    // 글
    private String contents;
//
//    // 장소
    private String location;
//
//    // 날씨
    private Long weather;
//
    private Float surfTemp;
    private Float underTemp;
    private String viewSight;
    private String tide;
//
//    // 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDiveTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDiveTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timeDiffDive;
//
//    // 수심 및 공기
    private Float avgDepDiff; // Changed to camelCase
    private Float maxDiff;
//
    private Long startBar;
    private Long endBar;
    private Long diffBar;
}
