package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.common.entity.Equipment;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LogbookResponse {
    private String date;
    private List<String> imageUrls;
    private String title;
    private String contents;
    private UserCondition userCondition;
    private Equipment equipment;

    @Builder
    public LogbookResponse(List<String> imageUrls, String title, String contents, String date, UserCondition userCondition, Equipment equipment) {
        this.imageUrls = imageUrls;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.userCondition = userCondition;
        this.equipment = equipment;
    }
}
