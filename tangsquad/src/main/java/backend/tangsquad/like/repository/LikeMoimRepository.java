package backend.tangsquad.like.repository;

import backend.tangsquad.like.entity.LikeLogbook;
import backend.tangsquad.like.entity.LikeMoim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeMoimRepository extends JpaRepository<LikeMoim, Long> {
    List<LikeMoim> findAllByUserId(Long userId);
}
