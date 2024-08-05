package backend.tangsquad.service;

import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;

import java.util.List;

public interface MoimService {
    void make(Moim moim);
    Moim findMoimById(Long moimId);

//    Moim findMoimByOwner(String owner);
    List<Moim> getMoims(User user);
}
