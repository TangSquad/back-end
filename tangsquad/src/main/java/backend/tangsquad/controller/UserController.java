package backend.tangsquad.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.RegisterRequestDto;
import backend.tangsquad.dto.response.RegisterResponse;
import backend.tangsquad.dto.response.WithdrawResponse;
import backend.tangsquad.service.AuthService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.service.VerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, VerificationService verificationService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponse response = userService.registerUser(registerRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        WithdrawResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(response);
    }
}
