package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.DoctorScheduleResponse;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.service.DoctorScheduleService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Doctor Schedule", description = "Doctor Schedule API")
@Slf4j(topic = "DOCTOR_SCHEDULE_CONTROLLER")
public class DoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor schedule by ID", description = "Retrieve a doctor schedule by its ID")
    public ResponseData<DoctorScheduleResponse> getDoctorScheduleById(@PathVariable("id") Long id) {
        log.info("Get doctor schedule request for id: {}", id);
        return ResponseData.<DoctorScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor schedule retrieved successfully")
                .data(doctorScheduleService.getDoctorScheduleById(id))
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new doctor schedule", description = "Create a new doctor schedule with provided details")
    public ResponseData<DoctorScheduleResponse> createDoctorSchedule(@RequestBody @Valid DoctorScheduleRequest request) {
        log.info("Create doctor schedule request: {}", request);
        return ResponseData.<DoctorScheduleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Doctor schedule created successfully")
                .data(doctorScheduleService.createDoctorSchedule(request))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor schedule", description = "Update an existing doctor schedule by its ID")
    public ResponseData<DoctorScheduleResponse> updateDoctorSchedule(
            @PathVariable("id") Long id,
            @RequestBody @Valid DoctorScheduleRequest request) {
        log.info("Update doctor schedule request for id: {}", id);
        return ResponseData.<DoctorScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor schedule updated successfully")
                .data(doctorScheduleService.updateDoctorSchedule(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor schedule", description = "Delete a doctor schedule by its ID")
    public ResponseData<Void> deleteDoctorSchedule(@PathVariable("id") Long id) {
        log.info("Delete doctor schedule request for id: {}", id);
        doctorScheduleService.deleteDoctorSchedule(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Doctor schedule deleted successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all doctor schedules", description = "Retrieve a paginated list of all doctor schedules")
    public ResponseData<PageResponse<DoctorScheduleResponse>> getAllDoctorSchedules(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Get all doctor schedules request: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
        return ResponseData.<PageResponse<DoctorScheduleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor schedules retrieved successfully")
                .data(doctorScheduleService.getAllDoctorSchedules(page, size, sort, direction))
                .build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search doctor schedules", description = "Search doctor schedules by doctor ID, date, and time slot with pagination")
    public ResponseData<PageResponse<DoctorScheduleResponse>> searchDoctorSchedules(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer timeSlotId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("Search doctor schedules request with doctorId: {}, date: {}, timeSlotId: {}", doctorId, date, timeSlotId);
        return ResponseData.<PageResponse<DoctorScheduleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor schedules search completed successfully")
                .data(doctorScheduleService.searchDoctorSchedules(doctorId, date, timeSlotId, page, size, sort, direction))
                .build();
    }

}