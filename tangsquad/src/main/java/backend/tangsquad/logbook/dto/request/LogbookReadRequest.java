package backend.tangsquad.logbook.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

public class LogbookReadRequest {

    private Long logId;
    private Long userId;
    private LocalDateTime date;
    private String title;
    private String contents;
    private String location;

    @Builder
    public LogbookReadRequest(Long logId, Long userId, LocalDateTime date, String title, String contents, String location, Long weather) {
        this.logId = logId;
        this.userId = userId;
        this.date = date;
        this.title = title;
        this.contents = contents;
        this.location = location;
    }

}
