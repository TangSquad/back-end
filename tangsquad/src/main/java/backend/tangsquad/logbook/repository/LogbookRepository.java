package backend.tangsquad.logbook.repository;


import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.entity.Logbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Long> {
    List<Logbook> findByUserId(Long userId);
    Optional<Logbook> findByIdAndUserId(Long logId, Long userId);
}
