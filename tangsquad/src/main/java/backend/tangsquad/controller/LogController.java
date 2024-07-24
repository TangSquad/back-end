package backend.tangsquad.controller;

import backend.tangsquad.dto.*;
import backend.tangsquad.service.LogService;
import backend.tangsquad.swagger.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LogController {

    private final LogService LogService;

    @GetMapping("/logs")
    public CommonResponse<LogListReadResponse> getLogs(@RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(required = false) String sort) {
        return CommonResponse.success(LogService.getLogs(page, size, sort));
    }

    @GetMapping("/logs/{id}")
    public CommonResponse<LogReadResponse> getLogs(@PathVariable Long id) {
        return CommonResponse.success(LogService.getLog(id));
    }

    @PostMapping("/logs")
    public CommonResponse<LogCreateResponse> createLog(@RequestBody LogCreateRequest request) {
        return CommonResponse.success(LogService.createLog(request));
    }

    @PutMapping("/logs/{id}")
    public CommonResponse<Void> updateLog(@PathVariable Long id, @RequestBody LogUpdateRequest request) {
        LogService.updateLog(id, request);
        return CommonResponse.success();
    }

    @DeleteMapping("/logs/{id}")
    public CommonResponse<LogReadResponse> deleteLog(@PathVariable Long id) {
        LogService.deleteLog(id);
        return CommonResponse.success();
    }
}
