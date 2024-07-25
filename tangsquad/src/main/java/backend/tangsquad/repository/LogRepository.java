package backend.tangsquad.repository;


import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository {
    Logbook save(Logbook logbook);
//    List<Logbook> findByUser(User user);
    Optional<Logbook> findById(String username, Long id);
    List<Logbook> findAll();
}
