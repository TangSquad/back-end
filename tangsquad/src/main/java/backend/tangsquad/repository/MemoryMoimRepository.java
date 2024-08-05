package backend.tangsquad.repository;

import backend.tangsquad.domain.Moim;

import java.util.HashMap;
import java.util.Map;

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
    public Moim findById(Long moimId) {
        return moimStorage.get(moimId);
    }

    @Override
    public Moim findByOwner(String owner) {
        for (Moim moim : moimStorage.values()) {
            if (moim.getMoimOwner().equals(owner)) {
                return moim;
            }
        }
        return null; // Return null if no matching Moim is found
    }
}
