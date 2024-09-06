package backend.tangsquad.logbook.repository;


import backend.tangsquad.logbook.entity.Logbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Long> {
    List<Logbook> findByUserId(Long userId);

        // Custom query to find a logbook by userId and logId
}
