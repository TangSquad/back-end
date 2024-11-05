package backend.tangsquad.logbook.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class LogbookCreateRequest {
    private LocalDateTime date;
    private Boolean isPublic;
    private String title;
    private String contents;
    private String location;

    @Builder
    public LogbookCreateRequest(LocalDateTime date,Boolean isPublic, String title, String contents, String location, Long weather) {
        this.date = date;
        this.isPublic = isPublic;
        this.title = title;
        this.contents = contents;
        this.location = location;
    }

}
