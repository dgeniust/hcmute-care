package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.CardiacTestRequest;
import vn.edu.hcmute.utecare.dto.response.CardiacTestResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.model.Account;

import vn.edu.hcmute.utecare.service.CardiacTestService;
import vn.edu.hcmute.utecare.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1/cardiac-tests")
@RequiredArgsConstructor
@Tag(name = "Cardiac Test", description = "Cardiac Test Management API")
@Slf4j(topic = "CARDIAC_TEST_CONTROLLER")
public class CardiacTestController {

    private final CardiacTestService cardiacTestService;

    @PostMapping
    @Operation(summary = "Create a new cardiac test", description = "Create a new cardiac test with the provided details")
    public ResponseData<CardiacTestResponse> createCardiacTest(@RequestBody @Valid CardiacTestRequest request) {
        log.info("Create cardiac test request: {}", request);
        return ResponseData.<CardiacTestResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Cardiac test created successfully")
                .data(cardiacTestService.createCardiacTest(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cardiac test by ID", description = "Retrieve a cardiac test by its ID")
    public ResponseData<CardiacTestResponse> getCardiacTestById(@PathVariable("id") Long id) {
        log.info("Get cardiac test request for id: {}", id);
        return ResponseData.<CardiacTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cardiac test retrieved successfully")
                .data(cardiacTestService.getCardiacTestById(id))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all cardiac tests", description = "Retrieve a paginated list of all cardiac tests")
    public ResponseData<PageResponse<CardiacTestResponse>> getAllCardiacTests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all cardiac tests request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<CardiacTestResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Cardiac tests retrieved successfully")
                .data(cardiacTestService.getAll(page, size, sort, direction))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a cardiac test", description = "Update an existing cardiac test by its ID")
    public ResponseData<CardiacTestResponse> updateCardiacTest(
            @PathVariable("id") Long id,
            @RequestBody @Valid CardiacTestRequest request) {
        log.info("Update cardiac test request for id: {}", id);
        return ResponseData.<CardiacTestResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cardiac test updated successfully")
                .data(cardiacTestService.updateCardiacTest(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cardiac test", description = "Delete a cardiac test by its ID")
    public ResponseData<Void> deleteCardiacTest(@PathVariable("id") Long id) {
        log.info("Delete cardiac test request for id: {}", id);
        cardiacTestService.deleteCardiacTest(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Cardiac test deleted successfully")
                .build();
    }

    @GetMapping("/test")
    @Operation(summary = "Test current user", description = "Retrieve the current authenticated user")
    public Account test() {
        return SecurityUtil.getCurrentUser();
    }
}