package backend.tangsquad.repository;

import backend.tangsquad.domain.Moim;

public interface MoimRepository {

    void save(Moim moim);
    Moim findById(Long moimId);

    Moim findByOwner(String owner);
}
