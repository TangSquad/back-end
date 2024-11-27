package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.logbook.entity.Log;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LogbookResponse {
    private Long logbookId;
    private Long userId;
    private String thumbnailUrl;
    private Boolean isPublic;
    private String title;
    private String contents;
    private LocalDateTime date;
    private String location;
    private List<Log> logs;

    @Builder
    public LogbookResponse(Long logbookId, Boolean isPublic, String thumbnailUrl, Long userId, String title, String contents, LocalDateTime date, String location, List<Log> logs) {
        this.logbookId = logbookId;
        this.isPublic = isPublic;
        this.thumbnailUrl = thumbnailUrl;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.location = location;
        this.logs = logs;
    }
}
