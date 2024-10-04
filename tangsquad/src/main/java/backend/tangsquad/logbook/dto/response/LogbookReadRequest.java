package backend.tangsquad.logbook.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogbookReadRequest {
    private Long id; // Assuming you want the log ID
    private String title;
    private String contents;
    private LocalDateTime date;
    private String location;
    private Long weather;
    private Float surfTemp;
    private Float underTemp;
}
