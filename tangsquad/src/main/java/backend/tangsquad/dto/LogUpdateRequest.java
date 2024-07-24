package backend.tangsquad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogUpdateRequest {
    private String title;
    private String content;
}
