package backend.tangsquad.repository;


import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Long> {
    List<Logbook> findByUserId(Long userId);

        // Custom query to find a logbook by userId and logId
}
