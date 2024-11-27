package backend.tangsquad.logbook.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.like.repository.LikeLogbookRepository;
import backend.tangsquad.logbook.dto.request.LogbookCreateRequest;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogbookRepository;
import backend.tangsquad.swagger.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogbookService {

    private final LogbookRepository logbookRepository;

    private LogbookResponse convertToLogbookResponse(Logbook logbook) {
        return LogbookResponse.builder()
                .logbookId(logbook.getId())
                .userId(logbook.getUser().getId())
                .title(logbook.getTitle())
                .contents(logbook.getContents())
                .date(logbook.getDate())
                .location(logbook.getLocation())
                .logs(logbook.getLogs())
                .build();
    }

    private LogbookRequest convertToLogbookRequest(Logbook logbook) {
        return LogbookRequest.builder()
                .id(logbook.getId())
                .isPublic(logbook.getIsPublic())
                .date(logbook.getDate())
                .thumbnailUrl(logbook.getThumbnailUrl())
                .contents(logbook.getContents())
                .location(logbook.getLocation())
                .title(logbook.getTitle())
                .build();
    }

    public LogbookResponse save(LogbookCreateRequest logbookCreateRequest, UserDetailsImpl userDetails) {
        try {
            Logbook logbook = Logbook.builder()
                    .user(userDetails.getUser())
                    .date(logbookCreateRequest.getDate())
                    .location(logbookCreateRequest.getLocation())
                    .title(logbookCreateRequest.getTitle())
                    .contents(logbookCreateRequest.getContents())
                    .build();

            Logbook savedLogbook = logbookRepository.save(logbook);

            return convertToLogbookResponse(savedLogbook);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while saving logbook.");
            return null;
        }
    }

    public List<LogbookResponse> getAllLogbooks() {
        List<Logbook> logbooks = logbookRepository.findAll();

        return logbooks.stream().map(logbook -> convertToLogbookResponse(logbook)
        ).collect(Collectors.toList());
    }

    public Logbook getLogbookByIdAndUserId(Long logbookId, Long userId) {

        return logbookRepository.findByIdAndUserId(logbookId, userId)
                .orElse(null);
    }

    public List<LogbookRequest> getLogbooksByUserId(Long userId) {

        try {
            List<Logbook> logbooks = logbookRepository.findByUserId(userId);

            List<LogbookRequest> logbookRequests = logbooks.stream()
                    .map(logbook -> convertToLogbookRequest(logbook))
                    .collect(Collectors.toList());
            return logbookRequests;

        } catch (Exception e) {
            return null;
        }
    }

    public Logbook getLogbookByLogbookId(Long logbookId) {

        try {
            Optional<Logbook> logbookOptional = logbookRepository.findById(logbookId);

            Logbook logbook = logbookOptional.get();

            return logbook;

        } catch (Exception e) {
            return null;
        }
    }


    public List<LogbookRequest> getLogbooksByLogbookId(List<Long> logbookIds) {
        try {
            List<Logbook> logbooks = logbookRepository.findAllById(logbookIds);

            return logbooks.stream()
                    .map(logbook -> convertToLogbookRequest(logbook))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return null;
        }
    }



    public LogbookResponse updateLogbook(LogbookRequest logbookRequest, UserDetailsImpl userDetails) {

        Optional<Logbook> logbookOptional = logbookRepository.findById(logbookRequest.getId());

        if (!logbookOptional.isPresent()) {
            throw new NoSuchElementException();
        }

        Logbook logbook = logbookOptional.get();

        if (userDetails.getUser() != logbook.getUser()) {
            return null;
        }
        logbook.update(logbookRequest);

        Logbook savedLogbook = logbookRepository.save(logbook);
        return convertToLogbookResponse(savedLogbook);
    }

    public ResponseEntity<CommonResponse> deleteLog(Long logbookId, UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the logbook from the service
            Optional<Logbook> logbookOptional = logbookRepository.findByIdAndUserId(logbookId, userDetails.getId());

            if (!logbookOptional.isPresent()) {
                // Log not found
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            }
            Logbook logbook = logbookOptional.get();
            Long logOwnerId = logbook.getUser().getId();

            if (!userDetails.getId().equals(logOwnerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            logbookRepository.delete(logbook);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void deleteAll() {
        try {
            List<Logbook> logbooks = logbookRepository.findAll();

            logbooks.stream().forEach(log -> {
                logbookRepository.delete(log);
            });

            System.out.println("All logs have been successfully processed and deleted.");
        } catch (Exception e) {
            System.err.println("An error occurred while deleting logs: " + e.getMessage());
        }
    }

}
