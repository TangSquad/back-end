package backend.tangsquad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEquipmentResponse {
    private String height;
    private String weight;
    private String suit;
    private String weightBelt;
    private String bc;
    private String shoes;
    private String mask;
}
