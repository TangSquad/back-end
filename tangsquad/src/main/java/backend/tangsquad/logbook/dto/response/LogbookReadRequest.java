package backend.tangsquad.logbook.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogbookReadRequest {
    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column
    private Long squadId;

    @Column
    private String contents;

    @Column
    private String location;

    @Column
    private Long weather;

    @Column
    private Float surfTemp;

    @Column
    private Float underTemp;
}
