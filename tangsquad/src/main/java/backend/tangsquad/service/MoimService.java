package backend.tangsquad.service;

import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.MoimUpdateRequest;

import java.util.List;

public interface MoimService {
    void make(Moim moim);
    Moim findMoimById(Long moimId);

//    Moim findMoimByOwner(String owner);
    List<Moim> getMoims(User user);

    void deleteMoim(String username, Long id);

    Moim updateMoim(String username, Long id, MoimUpdateRequest request);
}
