package backend.tangsquad.logbook.service;

import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.dto.request.LogUpdateRequest;
import backend.tangsquad.logbook.repository.LogbookRepository;
import backend.tangsquad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import java.util.List;
import java.util.Optional;

@Service
public class LogbookService {

    private final LogbookRepository logbookRepository;

    private UserRepository userRepository;

    @Autowired
    public LogbookService(LogbookRepository logbookRepository, UserRepository userRepository) {
        this.logbookRepository = logbookRepository;
        this.userRepository = userRepository;
    }

    public Logbook save(Logbook logbook) {
        // Save the logbook to the database
        return logbookRepository.save(logbook);
    }

    public List<Logbook> getLogs(Long userId)
    {
        return logbookRepository.findByUserId(userId);
    }

    public Optional<Logbook> getLog(Long logId) {
        return logbookRepository.findById(logId);
    }

//    public Optional<Logbook> getLog(Long userId, Long logId) {
//        return logbookRepository.findByUserId(userId, logId);
//    }

    // 수정 필요.
    public Logbook updateLog(Long logId, LogUpdateRequest request) {
        // Retrieve the existing Logbook by userId and logId
        Optional<Logbook> logbookOptional = logbookRepository.findById(logId);

        if (!logbookOptional.isPresent()) {
            throw new NoSuchElementException(", logId: " + logId);
        }

        // Get the existing logbook
        Logbook logbook = logbookOptional.get();

        // Update fields from the request if they are not null
        if (request.getTitle() != null) logbook.setTitle(request.getTitle());
        if (request.getId() != null) logbook.setId(request.getId());
        if (request.getDate() != null) logbook.setDate(request.getDate());
        if (request.getContents() != null) logbook.setContents(request.getContents());
        if (request.getLocation() != null) logbook.setLocation(request.getLocation());
        if (request.getWeather() != null) logbook.setWeather(request.getWeather());
        if (request.getSurfTemp() != null) logbook.setSurfTemp(request.getSurfTemp());
        if (request.getUnderTemp() != null) logbook.setUnderTemp(request.getUnderTemp());
        if (request.getViewSight() != null) logbook.setViewSight(request.getViewSight());
        if (request.getTide() != null) logbook.setTide(request.getTide());
        if (request.getStartDiveTime() != null) logbook.setStartDiveTime(request.getStartDiveTime());
        if (request.getEndDiveTime() != null) logbook.setEndDiveTime(request.getEndDiveTime());
        if (request.getTimeDiffDive() != null) logbook.setTimeDiffDive(request.getTimeDiffDive());
        if (request.getAvgDepDiff() != null) logbook.setAvgDepDiff(request.getAvgDepDiff());
        if (request.getMaxDiff() != null) logbook.setMaxDiff(request.getMaxDiff());
        if (request.getStartBar() != null) logbook.setStartBar(request.getStartBar());
        if (request.getEndBar() != null) logbook.setEndBar(request.getEndBar());
        if (request.getDiffBar() != null) logbook.setDiffBar(request.getDiffBar());

        // Save and return the updated logbook
        return logbookRepository.save(logbook);
    }

    public void deleteLog(Long logId) {
        // Retrieve the logbook based on logId
        Logbook logbook = logbookRepository.findById(logId)
                .orElseThrow(() -> new NoSuchElementException("Logbook not found for logId: " + logId));

        // Delete the logbook
        logbookRepository.delete(logbook);
    }


}
