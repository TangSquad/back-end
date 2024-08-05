package backend.tangsquad.service;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
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
