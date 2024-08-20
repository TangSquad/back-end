package backend.tangsquad.repository;

import backend.tangsquad.domain.Moim;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMoimRepository implements MoimRepository {
    private Map<Long, Moim> moimStorage = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public void save(Moim moim) {
        if (moim.getId() == null) {
            moim.setId(currentId++);
        }
        moimStorage.put(moim.getId(), moim);
    }

    @Override
    public Optional<Moim> findById(Long moimId) {
        return Optional.ofNullable(moimStorage.get(moimId));
    }

//    @Override
//    public Moim findByOwner(String owner) {
//        for (Moim moim : moimStorage.values()) {
//            if (moim.getMoimOwner().equals(owner)) {
//                return moim;
//            }
//        }
//        return null; // Return null if no matching Moim is found
//    }

//    @Override
//    public Optional<Moim> findById(Long moimId) {
//        return moimStorage.values().stream()
//                .filter(moim -> moim.getId().equals(moimId))
//                .findFirst();
//    }

    @Override
    public void delete(Moim moim) {
        moimStorage.remove(moim.getId());
    }


    @Override
    public List<Moim> findAll() {
        return new ArrayList<>(moimStorage.values());
    }

}
