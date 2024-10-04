package backend.tangsquad.logbook.repository;


import backend.tangsquad.logbook.entity.Logbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Long> {
    List<Logbook> findByUserId(Long userId);
    @Query("SELECT l FROM Logbook l WHERE l.id = :logId AND l.user.id = :userId")
    Optional<Logbook> findByIdAndUserId(@Param("logId") Long logId, @Param("userId") Long userId);

}
