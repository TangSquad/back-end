package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.diving.entity.Location;
import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class LogbookCreateRequest {
    private String date;
    private Boolean isPublic;
    private Location location;
    private List<String> imageUrls;
    private String title;
    private String contents;
    private UserCondition userCondition;

    @Builder
    public LogbookCreateRequest(String date, Boolean isPublic, Location location, List<String> imageUrls,String title, String contents, UserCondition userCondition) {
        this.date = date;
        this.isPublic = isPublic;
        this.location = location;
        this.imageUrls = imageUrls;
        this.title = title;
        this.contents = contents;
        this.userCondition = userCondition;
    }

}
