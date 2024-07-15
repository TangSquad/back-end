package backend.tangsquad.controller;

import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.RegisterRequestDto;
import backend.tangsquad.service.AuthService;
import backend.tangsquad.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        User savedUser = userService.registerUser(registerRequestDto);
        return ResponseEntity.ok(savedUser);
    }

}
