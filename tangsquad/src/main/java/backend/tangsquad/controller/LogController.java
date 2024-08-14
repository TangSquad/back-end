package backend.tangsquad.controller;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogCreateRequest;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.response.LogReadResponse;
import backend.tangsquad.repository.LogRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.LogService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.swagger.global.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


// 로그북 내비게이션 바 - 내 다이빙 (나의 로그 CRUD)
@RequestMapping("/logbook")
@RestController
public class LogController {

    private final LogService logService;

    private final LogRepository logRepository;

    private final UserRepository userRepository;

    @Autowired
    public LogController(LogService logService, LogRepository logRepository, UserRepository userRepository) {
        this.logService = logService;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

// Other imports...

    @PostMapping("")
    public ResponseEntity<Logbook> createLog(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LogCreateRequest request) {

        // Extract the username from the authenticated user's details
        String username = userDetails.getUsername();

        // Find the user in the database using the authenticated username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }

        // Create a new logbook entry
        Logbook logbook = new Logbook();
        User user = userOptional.get();
        logbook.setUser(user);

        // Set the current date and time
        logbook.setDate(LocalDateTime.now());

        // Populate the logbook with data from the request
        logbook.setTitle(request.getTitle());
        logbook.setContents(request.getContents());

        logbook.setLocation(request.getLocation());
        logbook.setWeather(request.getWeather());

        logbook.setSurfTemp(request.getSurfTemp());
        logbook.setUnderTemp(request.getUnderTemp());

        logbook.setViewSight(request.getViewSight());
        logbook.setTide(request.getTide());

        logbook.setStartDiveTime(request.getStartDiveTime());
        logbook.setEndDiveTime(request.getEndDiveTime());

        logbook.setTimeDiffDive(request.getTimeDiffDive());
        logbook.setAvgDepDiff(request.getAvgDepDiff());

        logbook.setMaxDiff(request.getMaxDiff());
        logbook.setStartBar(request.getStartBar());

        logbook.setEndBar(request.getEndBar());
        logbook.setDiffBar(request.getDiffBar());

        // Save the logbook to the database
        logRepository.save(logbook);

        // Return a response entity with the created logbook
        return ResponseEntity.ok(logbook);
    }


    @GetMapping("/")
    public List<Logbook> getMyLogs() {
        // Retrieve the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication is available and the principal is an instance of UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (!userOptional.isPresent()) {
                // Handle user not found, you can throw an exception or return a specific response
                throw new UsernameNotFoundException("User not found");
            }

            User user = userOptional.get();
            return logService.getLogs(user);
        }

        throw new UsernameNotFoundException("User is not authenticated");
    }

    @GetMapping("/{username}/")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public List<Logbook> getLogs(@PathVariable("username") String username,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();
        return logService.getLogs(user);
    }
    // Use @PathVariable for the ID since it's in the URL path
    @GetMapping("/{id}")
    public Optional<Logbook> getMyLog(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        return logService.getLog(username, id);
    }

    @GetMapping("/{username}/{id}")
    public ResponseEntity<Logbook> getLog(
            @PathVariable("username") String username,
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Extract the username from the authenticated user's details
        String currentUsername = userDetails.getUsername();

        // Retrieve the logbook entry by ID
        Optional<Logbook> logbookOptional = logService.getLog(username, id);

        if (logbookOptional.isPresent()) {
            Logbook logbook = logbookOptional.get();

            // Check if the logbook belongs to the requested user
            if (logbook.getUser().getUsername().equals(username)) {
                // Implement permission check logic if needed
                // For instance, if you allow access based on roles or specific permissions

                return ResponseEntity.ok(logbook);
            } else {
                // Return a forbidden status if the logbook does not belong to the requested user
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        // Return not found status if the logbook entry doesn't exist
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    // Use @PathVariable for the ID since it's in the URL path
    @PutMapping("/{id}")
    public Logbook updateLog(
            @PathVariable("id") Long id,
            @RequestBody LogUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        Logbook logbook = logService.updateLog(username, id, request);
        return logbook;
    }

    // Use @PathVariable for the ID since it's in the URL path
    @DeleteMapping("/{id}")
    public CommonResponse<LogReadResponse> deleteLog(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        logService.deleteLog(username, id);
        return CommonResponse.success();
    }
}
