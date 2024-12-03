package backend.tangsquad.logbook.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.converter.ConvertTo;
import backend.tangsquad.like.repository.LikeLogbookRepository;
import backend.tangsquad.logbook.dto.request.LogbookCreateRequest;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.request.ThumbnailRequest;
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

import static backend.tangsquad.converter.ConvertTo.convertToLogbookResponse;

@Service
@RequiredArgsConstructor
public class LogbookService {

    private final LogbookRepository logbookRepository;

    public LogbookResponse save(LogbookCreateRequest logbookCreateRequest, UserDetailsImpl userDetails) {
        try {
            Logbook logbook = Logbook.builder()
                    .user(userDetails.getUser())
                    .isPublic(logbookCreateRequest.getIsPublic())
                    .date(logbookCreateRequest.getDate())
                    .contents(logbookCreateRequest.getContents())
                    .imageUrls(logbookCreateRequest.getImageUrls())
                    .userCondition(logbookCreateRequest.getUserCondition())
                    .logbookEquipment(logbookCreateRequest.getLogbookEquipment())
                    .title(logbookCreateRequest.getTitle())
                    .build();

            if (logbookCreateRequest.getImageUrls() != null && logbookCreateRequest.getImageUrls().size() != 0) {
                String thumbnailUrl = logbookCreateRequest.getImageUrls().get(0);
                ThumbnailRequest thumbnailRequest = new ThumbnailRequest();
                thumbnailRequest.setThumbnailUrl(thumbnailUrl);
                logbook.updateThumbnail(thumbnailRequest);
            }

            Logbook savedLogbook = logbookRepository.save(logbook);

            return convertToLogbookResponse(savedLogbook, userDetails.getUser().getUserProfile());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while saving logbook.");
            return null;
        }
    }

    public List<LogbookResponse> getAllLogbooks(UserDetailsImpl userDetails) {
        List<Logbook> logbooks = logbookRepository.findAll();

        return logbooks.stream().map(logbook -> convertToLogbookResponse(logbook, userDetails.getUser().getUserProfile())
        ).collect(Collectors.toList());
    }

    public Logbook getLogbookByIdAndUserId(Long logbookId, Long userId) {

        return logbookRepository.findByIdAndUserId(logbookId, userId)
                .orElse(null);
    }

    public List<LogbookResponse> getLogbooksByUserId(UserDetailsImpl userDetails) {

        try {
            List<Logbook> logbooks = logbookRepository.findByUserId(userDetails.getUser().getId());

            return logbooks.stream()
                    .map(logbook -> convertToLogbookResponse(logbook, userDetails.getUser().getUserProfile()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return null;
        }
    }

    public LogbookResponse updateLogbook(LogbookRequest logbookRequest, UserDetailsImpl userDetails) {

        Logbook logbook = logbookRepository.findById(logbookRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("Logbook not found with id: " + logbookRequest.getId()));

        if (!userDetails.getUser().getId().equals(logbook.getUser().getId())) {
            throw null;
        }

        logbook.update(logbookRequest);

        Logbook savedLogbook = logbookRepository.save(logbook);

        return convertToLogbookResponse(savedLogbook, userDetails.getUser().getUserProfile());
    }

    public ResponseEntity<CommonResponse> deleteLog(Long logbookId, UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Optional<Logbook> logbookOptional = logbookRepository.findByIdAndUserId(logbookId, userDetails.getId());

            if (!logbookOptional.isPresent()) {
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
