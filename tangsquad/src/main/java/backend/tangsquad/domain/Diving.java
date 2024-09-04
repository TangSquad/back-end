package backend.tangsquad.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "diving")  // Ensure this matches your database table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Ensure this matches the column name in your database
    private Long divingId;

    @ManyToOne(fetch = FetchType.LAZY)
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
