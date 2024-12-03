package backend.tangsquad.logbook.dto.response;

import backend.tangsquad.logbook.entity.UserCondition;
import lombok.Data;

import java.util.List;

@Data
public class LogbookEquipment {
    private String suit;
    private String weightBelt;
    private String be;
    private String shoes;
    private String mask;
}
