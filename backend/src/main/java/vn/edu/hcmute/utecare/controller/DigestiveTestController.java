package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.DigestiveTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.dto.response.DigestiveTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.model.Account;

import vn.edu.hcmute.utecare.service.DigestiveTestService;
import vn.edu.hcmute.utecare.util.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/digestive-tests")
@RequiredArgsConstructor
@Tag(name = "Digestive Test", description = "Digestive Test Management API")
@Slf4j(topic = "DIGESTIVE_TEST_CONTROLLER")
public class DigestiveTestController {

    private final DigestiveTestService digestiveTestService;

    @PostMapping
    @Operation(summary = "Create a new digestive test", description = "Create a new digestive test with the provided details")
    public ResponseData<DigestiveTestResponse> createDigestiveTest(@RequestBody @Valid DigestiveTestRequest request) {
        log.info("Create digestive test request: {}", request);
        return ResponseData.<DigestiveTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Digestive test created successfully")
                .data(digestiveTestService.createDigestiveTest(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get digestive test by ID", description = "Retrieve a digestive test by its ID")
    public ResponseData<DigestiveTestResponse> getDigestiveTestById(@PathVariable("id") Long id) {
        log.info("Get digestive test request for id: {}", id);
        return ResponseData.<DigestiveTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Digestive test retrieved successfully")
                .data(digestiveTestService.getDigestiveTestById(id))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all digestive tests", description = "Retrieve a paginated list of all digestive tests")
    public ResponseData<PageResponse<DigestiveTestResponse>> getAllDigestiveTests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all digestive tests request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<DigestiveTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Digestive tests retrieved successfully")
                .data(digestiveTestService.getAll(page, size, sort, direction))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a digestive test", description = "Update an existing digestive test by its ID")
    public ResponseData<DigestiveTestResponse> updateDigestiveTest(
            @PathVariable("id") Long id,
            @RequestBody @Valid DigestiveTestRequest request) {
        log.info("Update digestive test request for id: {}", id);
        return ResponseData.<DigestiveTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Digestive test updated successfully")
                .data(digestiveTestService.updateDigestiveTest(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a digestive test", description = "Delete a digestive test by its ID")
    public ResponseData<Void> deleteDigestiveTest(@PathVariable("id") Long id) {
        log.info("Delete digestive test request for id: {}", id);
        digestiveTestService.deleteDigestiveTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Digestive test deleted successfully")
                .build();
    }

    @GetMapping("/test")
    @Operation(summary = "Test current user", description = "Retrieve the current authenticated user")
    public Account test() {
        return SecurityUtil.getCurrentUser();
    }


    @GetMapping("/by-date")
    @Operation(summary = "Lấy danh sách digestive test theo ngày", description = "Lấy danh sách digestive test của ngày được chỉ định với trạng thái PENDING.")
    public ResponseData<List<DigestiveTestResponse>> getAllLabTestByDateAndStatus(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status
    ) {
        log.info("Yêu cầu lấy danh sách LaboratoryTests theo ngày: {}", date);
        List<DigestiveTestResponse> responses = digestiveTestService.getAllLabTestByDateAndStatus(date, status);
        return ResponseData.<List<DigestiveTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Danh sách LaboratoryTests theo ngày được trả về thành công")
                .data(responses)
                .build();
    }
}