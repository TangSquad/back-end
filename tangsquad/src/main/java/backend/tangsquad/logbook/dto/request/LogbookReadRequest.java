package backend.tangsquad.logbook.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogbookReadRequest {

    private Long logId;
    private Long userId;
    private String thumbnailUrl;
    private Boolean isPublic;
    private LocalDateTime date;
    private String title;
    private String contents;
    private String location;

    @Builder
    public LogbookReadRequest(Long logId, Boolean isPublic, Long userId, String thumbnailUrl, LocalDateTime date, String title, String contents, String location, Long weather) {
        this.logId = logId;
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.date = date;
        this.title = title;
        this.contents = contents;
        this.location = location;
    }

}
