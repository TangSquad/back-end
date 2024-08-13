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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("")
    public Logbook createLog(@PathVariable("username") String username, @RequestBody LogCreateRequest request) {
        Logbook logbook = new Logbook();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }

        // LogCreateRequest 에 있는 데이터들을 보고 모두 request에서 가지고 와 저장하여야 함.
        User user = userOptional.get();
        logbook.setUser(user);
        logbook.setTitle(request.getTitle());
        logbook.setContents(request.getContents());
        logbook.setDate(request.getDate());

        logRepository.save(logbook);

        return logbook;
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
    public Optional<Logbook> getLog(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        return logService.getLog(username, id);
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
