package backend.tangsquad.diving.repository;

import backend.tangsquad.diving.entity.Diving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivingRepository extends JpaRepository<Diving, Long> {
    List<Diving> findByUserId(Long userId);
}
