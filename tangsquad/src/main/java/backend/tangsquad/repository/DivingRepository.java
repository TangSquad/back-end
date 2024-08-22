package backend.tangsquad.repository;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.Moim;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DivingRepository {
    Diving save(Diving diving);
    Diving findById(Long divingId);



//    Moim findByOwner(String owner);

    Optional<Diving> findById(String username, Long id);

    List<Diving> findAll();

    void delete(Diving diving);
}
