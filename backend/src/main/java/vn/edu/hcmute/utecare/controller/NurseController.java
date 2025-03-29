package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.NurseCreationRequest;
import vn.edu.hcmute.utecare.dto.request.NurseRequest;
import vn.edu.hcmute.utecare.dto.response.NurseResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.NurseService;

@RestController
@RequestMapping("/api/v1/nurses")
@RequiredArgsConstructor
@Tag(name = "Nurse", description = "Nurse API")
@Slf4j(topic = "NURSE_CONTROLLER")
public class NurseController {
    private final NurseService nurseService;

    @GetMapping("/{id}")
    @Operation(summary = "Get nurse by ID", description = "Retrieve a nurse by their ID")
    public ResponseData<NurseResponse> getNurseById(@PathVariable("id") Long id) {
        log.info("Get nurse request for id: {}", id);
        return ResponseData.<NurseResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Nurse retrieved successfully")
                .data(nurseService.getNurseById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new nurse", description = "Create a new nurse with provided details and associated account")
    public ResponseData<NurseResponse> createNurse(@RequestBody @Valid NurseCreationRequest request) {
        log.info("Create nurse request: {}", request);
        return ResponseData.<NurseResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Nurse created successfully")
                .data(nurseService.createNurse(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a nurse", description = "Update an existing nurse by their ID")
    public ResponseData<NurseResponse> updateNurse(
            @PathVariable("id") Long id,
            @RequestBody @Valid NurseRequest request) {
        log.info("Update nurse request for id: {}", id);
        return ResponseData.<NurseResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Nurse updated successfully")
                .data(nurseService.updateNurse(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a nurse", description = "Delete a nurse by their ID")
    public ResponseData<Void> deleteNurse(@PathVariable("id") Long id) {
        log.info("Delete nurse request for id: {}", id);
        nurseService.deleteNurse(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Nurse deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all nurses", description = "Retrieve a paginated list of all nurses")
    public ResponseData<PageResponse<NurseResponse>> getAllNurses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all nurses request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Nurses retrieved successfully")
                .data(nurseService.getAllNurses(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search nurses", description = "Search nurses by keyword (e.g., name, phone) with pagination")
    public ResponseData<PageResponse<NurseResponse>> searchNurses(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search nurses request with keyword: {}", keyword);
        return ResponseData.<PageResponse<NurseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Nurses search completed successfully")
                .data(nurseService.searchNurses(keyword, page, size, sort, direction))
                .build();
    }
}