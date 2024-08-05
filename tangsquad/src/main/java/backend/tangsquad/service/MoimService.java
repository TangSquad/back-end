package backend.tangsquad.service;

import backend.tangsquad.domain.Moim;

public interface MoimService {
    void make(Moim moim);
    Moim findMoimById(Long moimId);

    Moim findMoimByOwner(String owner);
}
