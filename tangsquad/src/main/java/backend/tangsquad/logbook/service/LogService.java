package backend.tangsquad.logbook.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.logbook.dto.request.LogCreateRequest;
import backend.tangsquad.logbook.dto.request.LogRequest;
import backend.tangsquad.logbook.dto.request.LogUpdateRequest;
import backend.tangsquad.logbook.dto.response.LogResponse;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogRepository;
import backend.tangsquad.logbook.repository.LogbookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final LogRepository logRepository;
    private final LogbookRepository logbookRepository;

    private List<LogResponse> convertToLogResponses(List<Log> logs) {
        return logs.stream().map(log -> convertToLogResponse(log)
        ).collect(Collectors.toList());
    }

    private LogResponse convertToLogResponse(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .whether(log.getWhether())
                .airTemp(log.getAirTemp())
                .surfTemp(log.getSurfTemp())
                .bottTemp(log.getBottTemp())
                .viewSight(log.getViewSight())
                .tide(log.getTide())
                .wave(log.getWave())
                .surge(log.getSurge())
                .startDiveTime(log.getStartDiveTime())
                .endDiveTime(log.getEndDiveTime())
                .diveTime(log.getDiveTime())
                .subject(log.getSubject())
                .avgDepDepth(log.getAvgDepDepth())
                .maxDepth(log.getMaxDepth())
                .startBar(log.getStartBar())
                .endBar(log.getEndBar())
                .logbookId(log.getLogbook().getId())
                .build();
    }

    public LogResponse save(LogCreateRequest logCreateRequest, UserDetailsImpl userDetails) {
        try {

            Optional<Logbook> logbookOptional = logbookRepository.findById(logCreateRequest.getLogbookId());

            if (logbookOptional.isEmpty()) return null;
            Logbook logbook = logbookOptional.get();

            System.out.println("logbookId: " + logbook.getId());
            System.out.println("logbook.getLogs().size(): " + logbook.getLogs().size());
            if (logbook.getLogs().size() >= 8 ) return null;

            Log log = Log.builder()
                    .user(userDetails.getUser())
                    .whether(logCreateRequest.getWhether())
                    .airTemp(logCreateRequest.getAirTemp())
                    .surfTemp(logCreateRequest.getSurfTemp())
                    .bottTemp(logCreateRequest.getBottTemp())
                    .viewSight(logCreateRequest.getViewSight())
                    .tide(logCreateRequest.getTide())
                    .wave(logCreateRequest.getWave())
                    .surge(logCreateRequest.getSurge())
                    .startDiveTime(logCreateRequest.getStartDiveTime())
                    .endDiveTime(logCreateRequest.getEndDiveTime())
                    .diveTime(logCreateRequest.getDiveTime())
                    .subject(logCreateRequest.getSubject())
                    .avgDepDepth(logCreateRequest.getAvgDepDepth())
                    .maxDepth(logCreateRequest.getMaxDepth())
                    .startBar(logCreateRequest.getStartBar())
                    .endBar(logCreateRequest.getEndBar())
                    .logbook(logbook)
                    .build();

            logRepository.save(log);
            System.out.println("log.getId().toString(): " + log.getId().toString());
            logbook.getLogs().add(log);
            logbookRepository.save(logbook);
            System.out.println("logbook.getLogs().size(): " + logbook.getLogs().size());

            return convertToLogResponse(log);


        } catch (Exception e) {
            logger.error("Error while saving log.", e);
            throw new RuntimeException("Error while saving log.");
        }
    }





    public List<LogResponse> getAllLogs() {
        List<Log> logs = logRepository.findAll();

        return convertToLogResponses(logs);
    }

    public List<LogResponse> getLogsInLogbook(Long logbookId) {
        try {

            Optional<Logbook> logbookOptional = logbookRepository.findById(logbookId);

            if (logbookOptional.isEmpty()) return null;
            Logbook logbook = logbookOptional.get();

            List<Log> logs = logRepository.findAll().stream()
                    .filter(log -> log.getLogbook() == logbook)
                    .collect(Collectors.toList());

            return convertToLogResponses(logs);
        } catch (Exception e) {
            logger.error("Error while getting log ID: " + logbookId, e);
            throw new RuntimeException("Error while retrieving log.");
        }
    }
    public LogResponse getLog(Long logId) {
        try {
            Log log = logRepository.findById(logId)
                    .orElseThrow(() -> new RuntimeException("Log not found"));

            return convertToLogResponse(log);
        } catch (Exception e) {
            logger.error("Error while getting log ID: " + logId, e);
            throw new RuntimeException("Error while retrieving log.");
        }
    }

    public LogResponse updateLog(LogUpdateRequest logUpdateRequest, UserDetailsImpl userDetails) {
        try {
            Log log = logRepository.findById(logUpdateRequest.getLogbook().getId())
                    .orElseThrow(() -> new RuntimeException("Log not found"));

            if (!log.getUser().getId().equals(userDetails.getUser().getId())) {
                throw new RuntimeException("Unauthorized to update this log.");
            }

            log.update(logUpdateRequest);
            logRepository.save(log);

            return convertToLogResponse(log);
        } catch (Exception e) {
            logger.error("Error while updating log ID: " + logUpdateRequest.getLogbook().getId(), e);
            throw new RuntimeException("Error while updating log.");
        }
    }
    public LogResponse delete(Long logId, UserDetailsImpl userDetails) {
        try {
            Log log = logRepository.findById(logId)
                    .orElseThrow(() -> new RuntimeException("Log not found"));

            if (!log.getUser().getId().equals(userDetails.getUser().getId())) {
                throw new RuntimeException("Unauthorized to delete this log.");
            }

            logRepository.delete(log);

            return convertToLogResponse(log);
        } catch (Exception e) {
            logger.error("Error while deleting log ID: " + logId, e);
            throw new RuntimeException("Error while deleting log.");
        }
    }

    public void deleteAll() {
        try {
            List<Log> logs = logRepository.findAll();

            logs.stream().forEach(log -> {
                logRepository.delete(log);
            });

            System.out.println("All logs have been successfully processed and deleted.");
        } catch (Exception e) {
            System.err.println("An error occurred while deleting logs: " + e.getMessage());
        }
    }
}
