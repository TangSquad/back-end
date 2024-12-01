package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LogbookReadRequest {

    private Long logId;
    private Long userId;
    private String thumbnailUrl;
    private Boolean isPublic;
    private LocalDateTime date;
    private String title;
    private String contents;
    private List<Long> logs;
    private UserCondition userCondition;

    @Builder
    public LogbookReadRequest(Long logId, Boolean isPublic, Long userId, String thumbnailUrl, LocalDateTime date, String title, List<Long> logs, String contents, UserCondition userCondition) {
        this.logId = logId;
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.date = date;
        this.title = title;
        this.logs = logs;
        this.contents = contents;
        this.userCondition = userCondition;
    }

}
