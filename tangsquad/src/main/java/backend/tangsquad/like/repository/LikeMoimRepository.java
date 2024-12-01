package backend.tangsquad.like.repository;

import backend.tangsquad.like.entity.LikeMoim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeMoimRepository extends JpaRepository<LikeMoim, Long> {
    List<LikeMoim> findAllByUserId(Long userId);
    Optional<LikeMoim> findByUserIdAndMoimId(Long userId, Long moimId);
    void deleteByUserIdAndMoimId(Long userId, Long moimId);
}
