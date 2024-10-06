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
    private String title;
    private String contents;
    private String location;

    @Builder
    public LogbookRequest(Long id, Long userId, LocalDateTime date, String title, String contents, String location, Long weather) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.contents = contents;
        this.location = location;
    }
}
