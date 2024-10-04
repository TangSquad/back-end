package backend.tangsquad.likes.repository;

import backend.tangsquad.likes.entity.LikeLogbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeLogbookRepository extends JpaRepository<LikeLogbook, Long> {
    List<LikeLogbook> findAllByUserId(Long userId);
}
