package backend.tangsquad.like.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "like_moim")
public class LikeMoim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long moimId;

    @Builder
    public LikeMoim(Long userId, Long moimId) {
        this.userId = userId;
        this.moimId = moimId;
    }
}
