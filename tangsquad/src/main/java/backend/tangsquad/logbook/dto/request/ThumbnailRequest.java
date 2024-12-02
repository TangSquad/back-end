package backend.tangsquad.logbook.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class ThumbnailRequest {
    private String thumbnailUrl;
}
