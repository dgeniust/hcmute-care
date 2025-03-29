package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.DoctorCreationRequest;
import vn.edu.hcmute.utecare.dto.request.DoctorRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.DoctorService;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor", description = "Doctor API")
@Slf4j(topic = "DOCTOR_CONTROLLER")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieve a doctor by their ID")
    public ResponseData<DoctorResponse> getDoctorById(@PathVariable("id") Long id) {
        log.info("Get doctor request for id: {}", id);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor retrieved successfully")
                .data(doctorService.getDoctorById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Create a new doctor with provided details and associated account")
    public ResponseData<DoctorResponse> createDoctor(@RequestBody @Valid DoctorCreationRequest request) {
        log.info("Create doctor request: {}", request);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Doctor created successfully")
                .data(doctorService.createDoctor(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor", description = "Update an existing doctor by their ID")
    public ResponseData<DoctorResponse> updateDoctor(
            @PathVariable("id") Long id,
            @RequestBody @Valid DoctorRequest request) {
        log.info("Update doctor request for id: {}", id);
        return ResponseData.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor updated successfully")
                .data(doctorService.updateDoctor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor", description = "Delete a doctor by their ID")
    public ResponseData<Void> deleteDoctor(@PathVariable("id") Long id) {
        log.info("Delete doctor request for id: {}", id);
        doctorService.deleteDoctor(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Doctor deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve a paginated list of all doctors")
    public ResponseData<PageResponse<DoctorResponse>> getAllDoctors(
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sort,
                                                                     @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all doctors request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors retrieved successfully")
                .data(doctorService.getAllDoctors(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search doctors", description = "Search doctors by keyword (e.g., name, specialty) with pagination")
    public ResponseData<PageResponse<DoctorResponse>> searchDoctors(
                                                                     @RequestParam String keyword,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sort,
                                                                     @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search doctors request with keyword: {}", keyword);
        return ResponseData.<PageResponse<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctors search completed successfully")
                .data(doctorService.searchDoctors(keyword, page, size, sort, direction))
                .build();
    }
}