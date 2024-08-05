package backend.tangsquad.service;

import backend.tangsquad.domain.Moim;
import backend.tangsquad.repository.MemoryMoimRepository;
import backend.tangsquad.repository.MoimRepository;

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
    public Moim findMoimByOwner(String owner) {
        return moimRepository.findByOwner(owner);
    }
}
