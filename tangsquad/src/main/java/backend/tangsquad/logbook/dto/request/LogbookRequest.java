package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.logbook.dto.response.LogbookEquipment;
import backend.tangsquad.logbook.entity.UserCondition;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LogbookRequest {
    private Long id;
    private String date;
    private List<String> imageUrls;
    private Boolean isPublic;
    private String title;
    private String contents;
    private UserCondition userCondition;
    private LogbookEquipment logbookEquipment;
    @Builder
    public LogbookRequest(Long id, Boolean isPublic, List<String> imageUrls, String date, String title, String contents, UserCondition userCondition, LogbookEquipment logbookEquipment) {
        this.id = id;
        this.isPublic = isPublic;
        this.imageUrls = imageUrls;
        this.date = date;
        this.title = title;
        this.contents = contents;
        this.userCondition = userCondition;
        this.logbookEquipment = logbookEquipment;
    }
}
