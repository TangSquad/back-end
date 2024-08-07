package backend.tangsquad.service;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogCreateRequest;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.response.LogCreateResponse;
import backend.tangsquad.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    public List<Logbook> getLogs(User user)
    {
        return logRepository.findAll().stream()
                .filter(logbook -> logbook.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public Optional<Logbook> getLog(String username, Long id) {
        return logRepository.findById(username, id);
    }

    public LogCreateResponse createLog(LogCreateRequest request) {
        return null;
    }

    // 수정 필요.
    public Logbook updateLog(String username, Long id, LogUpdateRequest request) {
        Logbook logbook = new Logbook();
        return logbook;
    }

    public void deleteLog(String username, Long id) {
    }
}
