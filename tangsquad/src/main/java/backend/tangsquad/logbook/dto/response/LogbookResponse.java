package backend.tangsquad.logbook.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogbookResponse {
    private Long logId; // Assuming you want the log ID
    private Long userId;
    private String title;
    private String contents;
    private LocalDateTime date;
    private String location;

    @Builder
    public LogbookResponse(Long logId, Long userId, String title, String contents, LocalDateTime date, String location) {
        this.logId = logId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.location = location;
    }
}
