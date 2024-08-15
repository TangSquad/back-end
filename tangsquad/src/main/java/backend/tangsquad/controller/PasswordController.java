package backend.tangsquad.controller;

import backend.tangsquad.dto.request.PasswordResetCodeRequest;
import backend.tangsquad.dto.request.PasswordResetEmailRequest;
import backend.tangsquad.dto.request.PasswordResetRequest;
import backend.tangsquad.dto.response.ApiResponse;
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
    public ApiResponse<Void> requestPasswordReset(@RequestBody PasswordResetEmailRequest request) {
        String email = request.getEmail();

        if (!userService.isEmailExists(email)) {
            return new ApiResponse<>(false, "Email does not exist.");
        }

        if(!verificationService.canResendEmailCode(email)) {
            return new ApiResponse<>(false, "Too many attempts. Please try again later.");
        }

        String code = verificationService.generateCode();
        verificationService.saveEmailVerificationCode(email, code);

        emailService.sendEmail(email, "Password Reset Code", "Your password reset code is: " + code);
        verificationService.incrementResendEmailAttempts(email);

        return new ApiResponse<>(true, "Code sent successfully.");
    }

    @Operation(summary = "비밀번호 재설정 코드 확인", description = "비밀번호 재설정 코드를 확인합니다.")
    @PostMapping("/verifyCode")
    public ApiResponse<Boolean> verifyCode(@RequestBody PasswordResetCodeRequest request) {
        boolean isValid = verificationService.verifyEmailCode(request.getEmail(), request.getCode());
        if (isValid) {
            return new ApiResponse<>(true, "Code verified successfully.", true);
        } else {
            return new ApiResponse<>(false, "Invalid code.", false);
        }
    }

    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정합니다.")
    @PostMapping("/reset")
    public ApiResponse<Void> resetPassword(@RequestBody PasswordResetRequest request) {
        if(!verificationService.verifyEmailCode(request.getEmail(), request.getCode())) {
            return new ApiResponse<>(false, "Invalid code.");
        } else {
            verificationService.deleteEmailCode(request.getEmail());
        }

        boolean isUpdated = userService.updatePassword(request.getEmail(), request.getNewPassword());
        if(!isUpdated) {
            return new ApiResponse<>(false, "Failed to reset password.");
        } else {
            return new ApiResponse<>(true, "Password reset successfully.");
        }
    }
}
