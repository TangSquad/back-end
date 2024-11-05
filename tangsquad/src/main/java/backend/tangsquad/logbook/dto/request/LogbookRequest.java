package backend.tangsquad.logbook.dto.request;

import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class LogbookRequest {
    private Long id;
    private LocalDateTime date;
    private String thumbnailUrl;
    private Boolean isPublic;
    private String title;
    private String contents;
    private String location;

    @Builder
    public LogbookRequest(Long id, Boolean isPublic, String thumbnailUrl, LocalDateTime date, String title, String contents, String location, Long weather) {
        this.id = id;
        this.isPublic = isPublic;
        this.thumbnailUrl = thumbnailUrl;
        this.date = date;
        this.title = title;
        this.contents = contents;
        this.location = location;
    }
}
