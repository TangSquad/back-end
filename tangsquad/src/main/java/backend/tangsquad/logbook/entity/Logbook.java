package backend.tangsquad.logbook.entity;

import backend.tangsquad.common.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "logbook")
@NoArgsConstructor
public class Logbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // In Logbook class
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column
    private Long squadId;

    @Column
    private String contents;

    @Column
    private String location;

    @Column
    private Long weather;

    @Column
    private Float surfTemp;

    @Column
    private Float underTemp;

    @Builder
    public Logbook(User user,  String location, String title, String contents) {
        this.user = user;
        this.location = location;
        this.title = title;
        this.contents = contents;
    }
}
