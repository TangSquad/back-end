package backend.tangsquad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogReadResponse {
    private Long id;
    private String title;
    private String content;
}

