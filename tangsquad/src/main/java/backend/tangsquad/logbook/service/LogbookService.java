package backend.tangsquad.logbook.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.logbook.dto.request.LogbookCreateRequest;
import backend.tangsquad.logbook.dto.request.LogbookReadRequest;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogbookRepository;
import backend.tangsquad.swagger.global.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LogbookService {

    private final LogbookRepository logbookRepository;

    public LogbookService(LogbookRepository logbookRepository) {
        this.logbookRepository = logbookRepository;
    }

    public LogbookResponse save(LogbookCreateRequest logbookCreateRequest, UserDetailsImpl userDetails) {
        try {
            // Build the logbook entity from the request
            Logbook logbook = Logbook.builder()
                    .user(userDetails.getUser())  // Set the user from authenticated user details
                    .date(logbookCreateRequest.getDate())  // Set the date
                    .location(logbookCreateRequest.getLocation())  // Set the location
                    .title(logbookCreateRequest.getTitle())  // Set the title
                    .contents(logbookCreateRequest.getContents())  // Set the contents
                    .build();

            // Save the logbook entity
            Logbook savedLogbook = logbookRepository.save(logbook);

            // Convert the saved entity to LogbookReadRequest
            LogbookResponse logbookResponse = LogbookResponse.builder()
                    .logId(savedLogbook.getId())  // Set the ID of the saved logbook
                    .userId(savedLogbook.getUser().getId())
                    .title(savedLogbook.getTitle())// Get the userId from the user
                    .contents(savedLogbook.getContents())
                    .date(savedLogbook.getDate())
                    .location(savedLogbook.getLocation())
                    .build();

            return logbookResponse;  // Return the read request

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while saving logbook.");
            return null;  // Handle the error and return null or throw a custom exception
        }
    }

    public Logbook getLogbookByIdAndUserId(Long logId, Long userId) {
        // Fetch the logbook by ID and ensure it belongs to the authenticated user
        return logbookRepository.findByIdAndUserId(logId, userId)
                .orElse(null); // Return null if not found
    }

    public List<LogbookRequest> getLogbooks(Long userId) {
        // Retrieve the logbooks for the authenticated user
        try {
            List<Logbook> logbooks = logbookRepository.findByUserId(userId);
            // Convert each Logbook to a LogbookRequest
            List<LogbookRequest> logbookRequests = logbooks.stream()
                    .map(logbook -> LogbookRequest.builder()
                            .userId(userId)  // Set the userId or user object
                            .contents(logbook.getContents())  // Map the contents of the logbook
                            .location(logbook.getLocation())  // Replace with actual location data if available in Logbook
                            .title(logbook.getTitle())  // Add title from Logbook
                            .build())
                    .collect(Collectors.toList());  // Collect all LogbookRequest objects into a list
            return logbookRequests;

        } catch (Exception e) {
            return null;
        }
    }

    // 수정 필요.
    public LogbookResponse updateLog(LogbookRequest logbookRequest, UserDetailsImpl userDetails) {
        // Retrieve the existing Logbook by userId and logId
        Optional<Logbook> logbookOptional = logbookRepository.findById(logbookRequest.getId());

        if (!logbookOptional.isPresent()) {
            throw new NoSuchElementException();
        }

        // Get the existing logbook
        Logbook logbook = logbookOptional.get();
        // Update the logbook using the new update method
        logbook.update(logbookRequest);

        Logbook savedLogbook = logbookRepository.save(logbook);
        LogbookResponse logbookResponse = LogbookResponse.builder()
                .logId(savedLogbook.getId())
                .userId(savedLogbook.getUser().getId())
                .date(savedLogbook.getDate())
                .title(savedLogbook.getTitle())
                .contents(savedLogbook.getContents())
                .location(savedLogbook.getLocation())
                .build();

        // Save and return the updated logbook
        return logbookResponse;
    }

    public ResponseEntity<CommonResponse> deleteLog(Long logId, UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Retrieve the logbook from the service
            Optional<Logbook> logbookOptional = logbookRepository.findByIdAndUserId(logId, userDetails.getId());

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

}
