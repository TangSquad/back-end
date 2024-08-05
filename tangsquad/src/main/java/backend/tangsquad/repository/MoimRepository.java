package backend.tangsquad.repository;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.Moim;

import java.util.List;
import java.util.Optional;

public interface MoimRepository {

    void save(Moim moim);
    Moim findById(Long moimId);

//    Moim findByOwner(String owner);

    Optional<Moim> findById(String username, Long id);

    List<Moim> findAll();

}
