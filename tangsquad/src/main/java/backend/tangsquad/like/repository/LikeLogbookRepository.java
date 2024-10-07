package backend.tangsquad.like.repository;

import backend.tangsquad.like.entity.LikeLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeLogbookRepository extends JpaRepository<LikeLogbook, Long> {
    List<LikeLogbook> findAllByUserId(Long userId);
}
