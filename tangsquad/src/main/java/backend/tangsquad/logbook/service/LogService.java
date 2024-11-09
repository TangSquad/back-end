package backend.tangsquad.logbook.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.logbook.dto.request.LogCreateRequest;
import backend.tangsquad.logbook.dto.request.LogRequest;
import backend.tangsquad.logbook.dto.response.LogResponse;
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

    public LogResponse save(LogCreateRequest logCreateRequest, UserDetailsImpl userDetails) {
        try {
            // Build the Log entity
            Log log = Log.builder()
                    .user(userDetails.getUser())
                    .viewSight(logCreateRequest.getViewSight())
                    .tide(logCreateRequest.getTide())
                    .startDiveTime(logCreateRequest.getStartDiveTime())
                    .endDiveTime(logCreateRequest.getEndDiveTime())
                    .timeDiffDive(logCreateRequest.getTimeDiffDive())
                    .avgDepDiff(logCreateRequest.getAvgDepDiff())
                    .maxDiff(logCreateRequest.getMaxDiff())
                    .startBar(logCreateRequest.getStartBar())
                    .endBar(logCreateRequest.getEndBar())
                    .diffBar(logCreateRequest.getDiffBar())
                    .logbookId(logCreateRequest.getLogbookId())
                    .build();

            logRepository.save(log);

            // Convert entity to response DTO
            return LogResponse.builder()
                    .id(log.getId())
                    .userId(log.getUser().getId())
                    .viewSight(log.getViewSight())
                    .tide(log.getTide())
                    .startDiveTime(log.getStartDiveTime())
                    .endDiveTime(log.getEndDiveTime())
                    .timeDiffDive(log.getTimeDiffDive())
                    .avgDepDiff(log.getAvgDepDiff())
                    .maxDiff(log.getMaxDiff())
                    .startBar(log.getStartBar())
                    .endBar(log.getEndBar())
                    .diffBar(log.getDiffBar())
                    .logbookId(log.getLogbookId())
                    .build();
        } catch (Exception e) {
            logger.error("Error while saving log.", e);
            throw new RuntimeException("Error while saving log.");
        }
    }

//    public List<LogResponse> getLogsInLogbook(Long logbookId, UserDetailsImpl userDetails) {
//        try {
//            // Retrieve Logbook
//            Logbook logbook = logbookRepository.findById(logbookId)
//                    .orElseThrow(() -> new RuntimeException("Logbook not found"));
//
//            // Retrieve Logs by logbookId
//            List<Log> logs = logRepository.findAllByLogbookId(logbookId);
//
//            // Convert each log entity to a response DTO
//            return logs.stream()
//                    .map(log -> LogResponse.builder()
//                            .id(log.getId())
//                            .userId(log.getUser().getId())
//                            .viewSight(log.getViewSight())
//                            .tide(log.getTide())
//                            .startDiveTime(log.getStartDiveTime())
//                            .endDiveTime(log.getEndDiveTime())
//                            .timeDiffDive(log.getTimeDiffDive())
//                            .avgDepDiff(log.getAvgDepDiff())
//                            .maxDiff(log.getMaxDiff())
//                            .startBar(log.getStartBar())
//                            .endBar(log.getEndBar())
//                            .diffBar(log.getDiffBar())
//                            .logbookId(log.getLogbookId())
//                            .build())
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            logger.error("Error while getting logs for logbook ID: " + logbookId, e);
//            throw new RuntimeException("Error while retrieving logs for logbook.");
//        }
//    }
//
//    public LogResponse getLog(Long logId) {
//        try {
//            Log log = logRepository.findById(logId)
//                    .orElseThrow(() -> new RuntimeException("Log not found"));
//
//            return LogResponse.builder()
//                    .id(log.getId())
//                    .userId(log.getUser().getId())
//                    .viewSight(log.getViewSight())
//                    .tide(log.getTide())
//                    .startDiveTime(log.getStartDiveTime())
//                    .endDiveTime(log.getEndDiveTime())
//                    .timeDiffDive(log.getTimeDiffDive())
//                    .avgDepDiff(log.getAvgDepDiff())
//                    .maxDiff(log.getMaxDiff())
//                    .startBar(log.getStartBar())
//                    .endBar(log.getEndBar())
//                    .diffBar(log.getDiffBar())
//                    .logbookId(log.getLogbookId())
//                    .build();
//        } catch (Exception e) {
//            logger.error("Error while getting log ID: " + logId, e);
//            throw new RuntimeException("Error while retrieving log.");
//        }
//    }
//
//    public LogResponse updateLog(LogRequest logRequest, UserDetailsImpl userDetails) {
//        try {
//            Log log = logRepository.findById(logRequest.getId())
//                    .orElseThrow(() -> new RuntimeException("Log not found"));
//
//            if (!log.getUser().getId().equals(userDetails.getUser().getId())) {
//                throw new RuntimeException("Unauthorized to update this log.");
//            }
//
//            // Update log fields
//            log.update(logRequest);
//
//            return LogResponse.builder()
//                    .id(log.getId())
//                    .userId(log.getUser().getId())
//                    .viewSight(log.getViewSight())
//                    .tide(log.getTide())
//                    .startDiveTime(log.getStartDiveTime())
//                    .endDiveTime(log.getEndDiveTime())
//                    .timeDiffDive(log.getTimeDiffDive())
//                    .avgDepDiff(log.getAvgDepDiff())
//                    .maxDiff(log.getMaxDiff())
//                    .startBar(log.getStartBar())
//                    .endBar(log.getEndBar())
//                    .diffBar(log.getDiffBar())
//                    .logbookId(log.getLogbookId())
//                    .build();
//        } catch (Exception e) {
//            logger.error("Error while updating log ID: " + logRequest.getId(), e);
//            throw new RuntimeException("Error while updating log.");
//        }
//    }
//
//    public LogResponse delete(Long logId, UserDetailsImpl userDetails) {
//        try {
//            Log log = logRepository.findById(logId)
//                    .orElseThrow(() -> new RuntimeException("Log not found"));
//
//            if (!log.getUser().getId().equals(userDetails.getUser().getId())) {
//                throw new RuntimeException("Unauthorized to delete this log.");
//            }
//
//            logRepository.delete(log);
//
//            return LogResponse.builder()
//                    .id(log.getId())
//                    .userId(log.getUser().getId())
//                    .viewSight(log.getViewSight())
//                    .tide(log.getTide())
//                    .startDiveTime(log.getStartDiveTime())
//                    .endDiveTime(log.getEndDiveTime())
//                    .timeDiffDive(log.getTimeDiffDive())
//                    .avgDepDiff(log.getAvgDepDiff())
//                    .maxDiff(log.getMaxDiff())
//                    .startBar(log.getStartBar())
//                    .endBar(log.getEndBar())
//                    .diffBar(log.getDiffBar())
//                    .logbookId(log.getLogbookId())
//                    .build();
//        } catch (Exception e) {
//            logger.error("Error while deleting log ID: " + logId, e);
//            throw new RuntimeException("Error while deleting log.");
//        }
//    }
}
