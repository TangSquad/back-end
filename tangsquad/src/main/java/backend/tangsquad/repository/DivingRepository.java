package backend.tangsquad.repository;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.Logbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivingRepository extends JpaRepository<Diving, Long> {
    List<Diving> findByUserId(Long userId);
}
