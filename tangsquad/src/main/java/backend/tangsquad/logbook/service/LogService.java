package backend.tangsquad.logbook.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.logbook.dto.request.LogRequest;
import backend.tangsquad.logbook.dto.response.LogResponse;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogRepository;
import backend.tangsquad.logbook.repository.LogbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final LogbookRepository logbookRepository;

    public LogResponse save(LogRequest logRequest, UserDetailsImpl userDetails) {
        try {
            Log log = Log.builder()
                    .user(userDetails.getUser())
                    .viewSight(logRequest.getViewSight())
                    .tide(logRequest.getTide())
                    .startDiveTime(logRequest.getStartDiveTime())
                    .endDiveTime(logRequest.getEndDiveTime())
                    .timeDiffDive(logRequest.getTimeDiffDive())
                    .avgDepDiff(logRequest.getAvgDepDiff())
                    .maxDiff(logRequest.getMaxDiff())
                    .startBar(logRequest.getStartBar())
                    .endBar(logRequest.getEndBar())
                    .diffBar(logRequest.getDiffBar())
                    .logbook(logRequest.getLogbook())
                    .build();

            LogResponse logResponse = LogResponse.builder()
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
                    .logbook(log.getLogbook())
                    .build();

            logRepository.save(log);
            return logResponse;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while saving log.");
            return null;
        }
    }

    public List<LogResponse> getLogsInLogbook(Long logbookId, UserDetailsImpl userDetails) {

        try {
            Optional<Logbook> optionalLogbook = logbookRepository.findById(logbookId);

            if (optionalLogbook.isEmpty()) return null;

            Logbook logbook = optionalLogbook.get();
            List<Log> logs = logRepository.findAllByLogbook(logbook);

            return logs.stream()
                    .map(log -> LogResponse.builder()
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
                            .logbook(log.getLogbook())
                            .build()
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while getting logs.");
            return null;
        }
    }

    public LogResponse getLog(Long logId, UserDetailsImpl userDetails) {

        try {
            Optional<Log> optionalLog = logRepository.findById(logId);

            if (optionalLog.isEmpty()) return null;

            Log log = optionalLog.get();

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
                    .logbook(log.getLogbook())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while getting log.");
            return null;
        }
    }

    public LogResponse updateLog(LogRequest logRequest, UserDetailsImpl userDetails) {
        try {

            Optional<Log> optionalLog = logRepository.findById(logRequest.getId());

            if (optionalLog.isEmpty()) return null;

            Log log = optionalLog.get();

            if (log.getUser().getId() != userDetails.getUser().getId()) return null;


            log.update(logRequest);

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
                    .logbook(log.getLogbook())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while updating log.");
            return null;
        }
    }

    public LogResponse delete(Long logId, UserDetailsImpl userDetails) {

        try {
            Optional<Log> optionalLog = logRepository.findById(logId);

            if (optionalLog.isEmpty()) return null;

            Log log = optionalLog.get();

            if (log.getUser().getId() != userDetails.getUser().getId()) return null;

            LogResponse logResponse = LogResponse.builder()
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
                    .logbook(log.getLogbook())
                    .build();

            logRepository.delete(log);

            return logResponse;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while deleting log.");
            return null;
        }
    }
}

