package backend.tangsquad.service;

import backend.tangsquad.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogService {

    public LogListReadResponse getLogs(Integer page,
                                       Integer size,
                                       String sort)
    {
        return null;
    }

    public LogReadResponse getLog(Long id) {
        return null;
    }

    public LogCreateResponse createLog(LogCreateRequest request) {
        return null;
    }

    public void updateLog(Long id, LogUpdateRequest request) {
    }

    public void deleteLog(Long id) {
    }
}
