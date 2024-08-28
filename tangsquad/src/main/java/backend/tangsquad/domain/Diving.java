package backend.tangsquad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="diving")
public class Diving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // figma 작업 완료 후 개발
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String licenseLimit;

    @Column
    private String moimLeader;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private String region;

    @Column
    private String theme;

}
