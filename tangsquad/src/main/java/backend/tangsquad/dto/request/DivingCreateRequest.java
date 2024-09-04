package backend.tangsquad.dto.request;

import backend.tangsquad.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

        private Long age;

        private String moodOne;

        private String moodTwo;
}