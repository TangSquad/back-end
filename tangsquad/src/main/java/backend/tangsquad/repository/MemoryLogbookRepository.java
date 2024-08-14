package backend.tangsquad.repository;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryLogRepository implements LogRepository{
    private static Map<Long, Logbook> logStore = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Logbook save(Logbook logbook) {
        logbook.setId(++sequence);
        logStore.put(logbook.getId(), logbook);
        return logbook;
    }

//    @Override
//    public List<Logbook> findByUser(User user) {
//        return store.values().stream()
//                .filter(logbook -> logbook.getUser().equals(user))
//                .collect(Collectors.toList());
//    }

    @Override
    public Optional<Logbook> findById(String username, Long id) {
        return logStore.values().stream()
                .filter(logbook -> logbook.getUser().getUsername().equals(username) && logbook.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Logbook> findByUserId(Long userId, Long logId) {
        return logStore.values().stream()
                .filter(logbook -> logbook.getUser().getId().equals(userId) && logbook.getId().equals(logId))
                .findFirst();
    }

    @Override
    public List<Logbook> findAll() {
        return new ArrayList<>(logStore.values());
    }
}
