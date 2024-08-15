package backend.tangsquad.controller;

import backend.tangsquad.dto.request.PasswordResetCodeRequest;
import backend.tangsquad.dto.request.PasswordResetEmailRequest;
import backend.tangsquad.dto.request.PasswordResetRequest;
import backend.tangsquad.service.EmailService;
import backend.tangsquad.service.UserService;
import backend.tangsquad.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@Tag(name = "Password", description = "비밀번호 재설정 관련 API")
public class PasswordController {

    private final EmailService emailService;
    private final VerificationService verificationService;
    private final UserService userService;

    @Autowired
    public PasswordController(EmailService emailService, VerificationService verificationService, UserService userService) {
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.userService = userService;
    }

    @Operation(summary = "비밀번호 재설정 코드 전송", description = "비밀번호 재설정 코드를 전송합니다.")
    @PostMapping("/sendCode")
    public String requestPasswordReset(@RequestBody PasswordResetEmailRequest request) {
        String email = request.getEmail();

        if (!userService.isUserExists(email)) {
            return "User does not exist.";
        }

        if(!verificationService.canResendCode(email)) {
            return "Too many attempts. Please try again later.";
        }

        String code = verificationService.generateCode();
        verificationService.saveVerificationCode(email, code);

        emailService.sendEmail(email, "Password Reset Code", "Your password reset code is: " + code);
        verificationService.incrementResendAttempts(email);

        return "Code sent successfully.";
    }

    @Operation(summary = "비밀번호 재설정 코드 확인", description = "비밀번호 재설정 코드를 확인합니다.")
    @PostMapping("/verifyCode")
    public boolean verifyCode(@RequestBody PasswordResetCodeRequest request) {
        return verificationService.verifyCode(request.getEmail(), request.getCode());
    }

    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정합니다.")
    @PostMapping("/reset")
    public String resetPassword(@RequestBody PasswordResetRequest request) {
        if(!verificationService.verifyCode(request.getEmail(), request.getCode())) {
            return "Invalid code.";
        } else {
            verificationService.deleteCode(request.getEmail());
        }

        boolean isUpdated = userService.updatePassword(request.getEmail(), request.getNewPassword());
        if(!isUpdated) {
            return "Failed to reset password.";
        } else {
            return "Password reset successfully.";
        }
    }
}
