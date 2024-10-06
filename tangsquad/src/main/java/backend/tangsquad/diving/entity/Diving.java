package backend.tangsquad.diving.entity;

import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Builder
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
    private String age;
    private String moodOne;
    private String moodTwo;
    private Long limitPeople;
    private String limitLicense;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String location;

    public Diving(User user, String divingName, String divingIntro, String age, String moodOne, String moodTwo, Long limitPeople, String limitLicense, LocalDate startDate, LocalDate endDate, String location) {
        this.user = user;
        this.divingName = divingName;
        this.divingIntro = divingIntro;
        this.age = age;
        this.moodOne = moodOne;
        this.moodTwo = moodTwo;
        this.limitPeople = limitPeople;
        this.limitLicense = limitLicense;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
}
