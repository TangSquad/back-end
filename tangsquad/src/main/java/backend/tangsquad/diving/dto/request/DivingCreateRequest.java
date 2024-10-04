package backend.tangsquad.diving.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivingCreateRequest {


        private String divingName;

        private String divingIntro;

        @JsonFormat(pattern = "yyyy-MM-dd") // JSON formatting for serialization/deserialization
        private LocalDate startDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // JSON formatting for serialization/deserialization
        private LocalDate endDate;

        private Long limitPeople;

        private String limitLicense;

        private String location;

        private String age;

        private String moodOne;

        private String moodTwo;
}
