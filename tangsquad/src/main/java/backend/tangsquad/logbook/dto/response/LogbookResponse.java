package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.common.entity.Equipment;
import backend.tangsquad.diving.entity.Location;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LogbookResponse {
    private Long id;
    private String date;
    private List<String> imageUrls;
    private String title;
    private String contents;
    private UserCondition userCondition;
    private LogbookEquipment equipment;
    private List<Location> locations;

    @Builder
    public LogbookResponse(Long id, List<String> imageUrls, String title, String contents, String date, UserCondition userCondition, LogbookEquipment equipment, List<Location> locations) {
        this.id = id;
        this.imageUrls = imageUrls;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.userCondition = userCondition;
        this.equipment = equipment;
        this.locations = locations;
    }
}
