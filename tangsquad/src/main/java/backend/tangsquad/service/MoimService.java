package backend.tangsquad.service;

import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.MoimUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface MoimService {
    void make(Moim moim);
    Optional<Moim> findMoimById(Long moimId);

//    Moim findMoimByOwner(String owner);
    List<Moim> getMoims(User user);

    void deleteMoim(Long currentUserId, Long moimId);

    // 모임을 생성한 유저만이 사용할 수 있는 기능.
    Moim updateMoim(Long moimId, MoimUpdateRequest request, Long userId);
}
