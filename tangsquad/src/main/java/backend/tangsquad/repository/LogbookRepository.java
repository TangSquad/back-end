package backend.tangsquad.repository;


import backend.tangsquad.domain.Logbook;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogbookRepository {
    Logbook save(Logbook logbook);
//    List<Logbook> findByUser(User user);
    Optional<Logbook> findById(String username, Long id);

    Optional<Logbook> findByUserId(Long userId, Long logId);
    List<Logbook> findAll();

    void delete(Logbook logbook);
}
