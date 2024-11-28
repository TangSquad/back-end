package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class LogbookCreateRequest {
    private LocalDateTime date;
    private Boolean isPublic;
    private String thumbnailUrl;
    private String title;
    private String contents;
    private String location;

    private UserCondition userCondition;

    @Builder
    public LogbookCreateRequest(LocalDateTime date, Boolean isPublic, String thumbnailUrl,String title, String contents, String location, UserCondition userCondition) {
        this.date = date;
        this.isPublic = isPublic;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.contents = contents;
        this.location = location;
        this.userCondition = userCondition;
    }

}
