package backend.tangsquad.like.repository;

import backend.tangsquad.like.entity.LikeLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeLogbookRepository extends JpaRepository<LikeLogbook, Long> {
    Optional<List<LikeLogbook>> findAllByUserId(Long userId);

    Optional<LikeLogbook> findByLogbookId(Long logbookId);
    List<LikeLogbook> findAllByLogbookId(Long logbookId);
    Optional<LikeLogbook> findByUserIdAndLogbookId(Long userId, Long logbookId);
}
