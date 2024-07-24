package backend.tangsquad.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDto {

    @NonNull
    private String organization;

    @NonNull
    private String grade;

    @NonNull
    private String imageUrl;
}
