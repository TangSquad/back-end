package backend.tangsquad.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogCreateRequest;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.response.LogCreateResponse;
import backend.tangsquad.repository.LogbookRepository;
import backend.tangsquad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Logbook> getLogs(User user)
    {
        return logbookRepository.findAll().stream()
                .filter(logbook -> logbook.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public Optional<Logbook> getLog(Long logId) {
        return logbookRepository.findById(logId);
    }

//    public Optional<Logbook> getLog(Long userId, Long logId) {
//        return logbookRepository.findByUserId(userId, logId);
//    }

    // 수정 필요.
//    public Logbook updateLog(Long userId, Long logId, LogUpdateRequest request) {
//        // Retrieve the existing Logbook
//        Optional<Logbook> logbookOptional = logbookRepository.findByUserId(userId, logId);
//
//        if (!logbookOptional.isPresent()) {
//            throw new NoSuchElementException("Logbook not found for userId: " + userId + ", logId: " + logId);
//        }
//
//        // Get the existing logbook
//        Logbook logbook = logbookOptional.get();
//
//        // Update fields from the request
//        if (request.getTitle() != null) {
//            logbook.setTitle(request.getTitle());
//        }
//        if (request.getSquadId() != null) {
//            logbook.setSquadId(request.getSquadId());
//        }
//        if (request.getDate() != null) {
//            logbook.setDate(request.getDate());
//        }
//        if (request.getContents() != null) {
//            logbook.setContents(request.getContents());
//        }
//        if (request.getLocation() != null) {
//            logbook.setLocation(request.getLocation());
//        }
//        if (request.getWeather() != null) {
//            logbook.setWeather(request.getWeather());
//        }
//        if (request.getSurfTemp() != null) {
//            logbook.setSurfTemp(request.getSurfTemp());
//        }
//        if (request.getUnderTemp() != null) {
//            logbook.setUnderTemp(request.getUnderTemp());
//        }
//        if (request.getViewSight() != null) {
//            logbook.setViewSight(request.getViewSight());
//        }
//        if (request.getTide() != null) {
//            logbook.setTide(request.getTide());
//        }
//        if (request.getStartDiveTime() != null) {
//            logbook.setStartDiveTime(request.getStartDiveTime());
//        }
//        if (request.getEndDiveTime() != null) {
//            logbook.setEndDiveTime(request.getEndDiveTime());
//        }
//        if (request.getTimeDiffDive() != null) {
//            logbook.setTimeDiffDive(request.getTimeDiffDive());
//        }
//        if (request.getAvgDepDiff() != null) {
//            logbook.setAvgDepDiff(request.getAvgDepDiff());
//        }
//        if (request.getMaxDiff() != null) {
//            logbook.setMaxDiff(request.getMaxDiff());
//        }
//        if (request.getStartBar() != null) {
//            logbook.setStartBar(request.getStartBar());
//        }
//        if (request.getEndBar() != null) {
//            logbook.setEndBar(request.getEndBar());
//        }
//        if (request.getDiffBar() != null) {
//            logbook.setDiffBar(request.getDiffBar());
//        }
//
//        // Save and return the updated logbook
//        return logbookRepository.save(logbook);
//    }


//    public void deleteLog(Long userId, Long logId) {
//        // Retrieve the logbook based on userId and logId
//        Optional<Logbook> logbookOptional = logbookRepository.findByUserId(userId, logId);
//
//        // Check if the logbook exists
//        if (!logbookOptional.isPresent()) {
//            throw new NoSuchElementException("Logbook not found for userId: " + userId + ", logId: " + logId);
//        }
//
//        // Get the logbook
//        Logbook logbook = logbookOptional.get();
//
//        // Check if the logbook belongs to the user
//        if (!logbook.getUser().getId().equals(userId)) {
//            throw new AccessDeniedException("Logbook does not belong to userId: " + userId);
//        }
//
//        // Delete the logbook
//        logbookRepository.delete(logbook);
//    }

}
