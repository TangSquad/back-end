package backend.tangsquad.repository;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.Moim;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryDivingRepository implements DivingRepository {
    private Map<Long, Diving> divingStorage = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public void save(Diving diving) {
        if (diving.getId() == null) {
            diving.setId(currentId++);
        }
        divingStorage.put(diving.getId(), diving);
    }

    @Override
    public Diving findById(Long divingId) {
        return divingStorage.get(divingId);
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

    @Override
    public Optional<Diving> findById(String username, Long id) {
        return divingStorage.values().stream()
                .filter(diving -> diving.getUser().getUsername().equals(username) && diving.getId().equals(id))
                .findFirst();
    }


    @Override
    public List<Diving> findAll() {
        return new ArrayList<>(divingStorage.values());
    }

}
