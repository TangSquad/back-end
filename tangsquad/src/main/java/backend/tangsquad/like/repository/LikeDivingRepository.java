package backend.tangsquad.like.repository;

import backend.tangsquad.like.entity.LikeDiving;
import backend.tangsquad.like.entity.LikeMoim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeDivingRepository extends JpaRepository<LikeDiving, Long> {
    List<LikeDiving> findAllByUserId(Long userId);
    Optional<LikeDiving> findByUserIdAndDivingId(Long userId, Long divingId);
}
