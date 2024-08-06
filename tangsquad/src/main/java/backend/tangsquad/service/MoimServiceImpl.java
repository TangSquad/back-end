package backend.tangsquad.service;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.repository.MemoryMoimRepository;
import backend.tangsquad.repository.MoimRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MoimServiceImpl implements MoimService {
    private final MoimRepository moimRepository = new MemoryMoimRepository();

    @Override
    public void make(Moim moim) {
        moimRepository.save(moim);
    }

    @Override
    public Moim findMoimById(Long moimId) {
        return moimRepository.findById(moimId);
    }

    @Override
    public void deleteMoim(String username, Long id) {
    }

    // 수정 필요
    @Override
    public Moim updateMoim(String username, Long id, MoimUpdateRequest request) {
        Moim moim = new Moim();
        return moim;
    }

//    @Override
//    public Moim findMoimByOwner(String owner) {
//        return moimRepository.findById(owner);
//    }
    @Override
    public List<Moim> getMoims(User user)
    {
        return moimRepository.findAll().stream()
                .filter(logbook -> logbook.getUser().equals(user))
                .collect(Collectors.toList());
    }

}
