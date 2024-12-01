package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LogbookResponse {
    private Long id;
    private Long userId;
    private String thumbnailUrl;
    private Boolean isPublic;
    private String title;
    private String contents;
    private LocalDateTime date;
    private List<Long> logIds;
    private UserCondition userCondition;

    @Builder
    public LogbookResponse(Long id, Boolean isPublic, String thumbnailUrl, Long userId, String title, String contents, LocalDateTime date, List<Long> logIds, UserCondition userCondition) {
        this.id = id;
        this.isPublic = isPublic;
        this.thumbnailUrl = thumbnailUrl;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.logIds = logIds;
        this.userCondition = userCondition;
    }
}
