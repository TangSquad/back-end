package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.UserCondition;
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
    private List<Log> logs;
    private UserCondition userCondition;

    @Builder
    public LogbookResponse(Long logbookId, Boolean isPublic, String thumbnailUrl, Long userId, String title, String contents, LocalDateTime date, List<Log> logs, UserCondition userCondition) {
        this.logbookId = logbookId;
        this.isPublic = isPublic;
        this.thumbnailUrl = thumbnailUrl;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.logs = logs;
        this.userCondition = userCondition;
    }
}
