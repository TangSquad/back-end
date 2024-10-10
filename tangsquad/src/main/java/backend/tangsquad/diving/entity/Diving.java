package backend.tangsquad.diving.entity;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


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

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "moods")
    private List<String> moods;

    private Long limitPeople;
    private String limitLicense;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String location;

    public void update(DivingRequest divingRequest) {
        if (divingRequest.getDivingName() != null) this.divingName = divingRequest.getDivingName();
        if (divingRequest.getDivingIntro() != null) this.divingIntro = divingRequest.getDivingIntro();
        if (divingRequest.getAge() != null) this.age = divingRequest.getAge();
        if (divingRequest.getMoods() != null) this.moods = divingRequest.getMoods();
        if (divingRequest.getLimitPeople() != null) this.limitPeople = divingRequest.getLimitPeople();
        if (divingRequest.getLimitLicense() != null) this.limitLicense = divingRequest.getLimitLicense();
        if (divingRequest.getStartDate() != null) this.startDate = divingRequest.getStartDate();
        if (divingRequest.getEndDate() != null) this.endDate = divingRequest.getEndDate();
        if (divingRequest.getLocation() != null) this.location = divingRequest.getLocation();
    }


    public Diving(User user, String divingName, String divingIntro, String age, List<String> moods, Long limitPeople, String limitLicense, LocalDate startDate, LocalDate endDate, String location) {
        this.user = user;
        this.divingName = divingName;
        this.divingIntro = divingIntro;
        this.age = age;
        this.moods = moods;
        this.limitPeople = limitPeople;
        this.limitLicense = limitLicense;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
}
