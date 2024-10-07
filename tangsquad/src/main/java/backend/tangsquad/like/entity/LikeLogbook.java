package backend.tangsquad.like.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "like_logbook")
public class LikeLogbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long logbookId;

    @Builder
    public LikeLogbook(Long userId, Long logbookId) {
        this.userId = userId;
        this.logbookId = logbookId;
    }
}
