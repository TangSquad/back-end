package backend.tangsquad.dto.response;

import backend.tangsquad.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DivingCreateResponse {
    private Long divingId;
    @JoinColumn(name = "user_id", nullable = false)  // Ensure the join column matches the foreign key column in the database
    private User user;
    private String divingName;
    private String divingIntro;
    private Long age;
    private String moodOne;
    private String moodTwo;
    private Long limitPeople;
    private String limitLicense;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String location;
}
