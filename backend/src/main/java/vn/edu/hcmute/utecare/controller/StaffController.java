package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.StaffCreationRequest;
import vn.edu.hcmute.utecare.dto.request.StaffRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.StaffResponse;
import vn.edu.hcmute.utecare.service.StaffService;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff API")
@Slf4j(topic = "STAFF_CONTROLLER")
public class StaffController {
    private final StaffService staffService;

    @GetMapping("/{id}")
    @Operation(summary = "Get staff by ID", description = "Retrieve a staff member by their ID")
    public ResponseData<StaffResponse> getStaffById(@PathVariable("id") Long id) {
        log.info("Get staff request for id: {}", id);
        return ResponseData.<StaffResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Staff retrieved successfully")
                .data(staffService.getStaffById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new staff", description = "Create a new staff member with provided details and associated account")
    public ResponseData<StaffResponse> createStaff(@RequestBody @Valid StaffCreationRequest request) {
        log.info("Create staff request: {}", request);
        return ResponseData.<StaffResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Staff created successfully")
                .data(staffService.createStaff(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a staff", description = "Update an existing staff member by their ID")
    public ResponseData<StaffResponse> updateStaff(
            @PathVariable("id") Long id,
            @RequestBody @Valid StaffRequest request) {
        log.info("Update staff request for id: {}", id);
        return ResponseData.<StaffResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Staff updated successfully")
                .data(staffService.updateStaff(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a staff", description = "Delete a staff member by their ID")
    public ResponseData<Void> deleteStaff(@PathVariable("id") Long id) {
        log.info("Delete staff request for id: {}", id);
        staffService.deleteStaff(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Staff deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all staff", description = "Retrieve a paginated list of all staff members")
    public ResponseData<PageResponse<StaffResponse>> getAllStaff(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all staff request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<StaffResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Staff retrieved successfully")
                .data(staffService.getAllStaff(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search staff", description = "Search staff members by keyword (e.g., name, role) with pagination")
    public ResponseData<PageResponse<StaffResponse>> searchStaff(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search staff request with keyword: {}", keyword);
        return ResponseData.<PageResponse<StaffResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Staff search completed successfully")
                .data(staffService.searchStaff(keyword, page, size, sort, direction))
                .build();
    }
}