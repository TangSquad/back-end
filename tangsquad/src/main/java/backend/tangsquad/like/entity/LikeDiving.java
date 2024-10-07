package backend.tangsquad.like.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "like_diving")
public class LikeDiving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long divingId;

    @Builder
    public LikeDiving(Long userId, Long divingId) {
        this.userId = userId;
        this.divingId = divingId;
    }
}
