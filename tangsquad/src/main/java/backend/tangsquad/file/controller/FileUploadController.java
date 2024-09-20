package backend.tangsquad.file.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.file.dto.response.FileReadResponse;
import backend.tangsquad.file.service.S3FileUploadService;
import backend.tangsquad.swagger.global.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "파일드업로 관련 API")
public class FileUploadController {
    private final S3FileUploadService s3FileUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "파일 업로드",
            description = "새로운 파일을 업로드합니다.",
            security = @SecurityRequirement(name = "AccessToken")
    )

    public CommonResponse<FileReadResponse> uploadFile(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String url = s3FileUploadService.uploadFile(file);
        return CommonResponse.success(new FileReadResponse(url));
    }
}
