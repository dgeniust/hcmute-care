package vn.edu.hcmute.utecare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest;
import vn.edu.hcmute.utecare.dto.response.PageResponse;
import vn.edu.hcmute.utecare.dto.response.ResponseData;
import vn.edu.hcmute.utecare.dto.response.ScheduleInfoResponse;
import vn.edu.hcmute.utecare.dto.response.ScheduleResponse;
import vn.edu.hcmute.utecare.service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "Schedule API")
@Slf4j(topic = "SCHEDULE_CONTROLLER")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Create a new schedule", description = "Create a new schedule with the provided details")
    public ResponseData<ScheduleResponse> createSchedule(@RequestBody @Valid ScheduleRequest request) {
        log.info("Create schedule request: {}", request);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Schedule created successfully")
                .data(scheduleService.createSchedule(request))
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Update an existing schedule", description = "Update the details of a schedule by its ID")
    public ResponseData<ScheduleResponse> updateSchedule(
            @PathVariable("id") Long id,
            @RequestBody @Valid ScheduleRequest request) {
        log.info("Update schedule request for ID: {}, request: {}", id, request);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule updated successfully")
                .data(scheduleService.updateSchedule(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Delete a schedule", description = "Delete a schedule by its ID")
    public ResponseData<Void> deleteSchedule(@PathVariable("id") Long id) {
        log.info("Delete schedule request for ID: {}", id);
        scheduleService.deleteSchedule(id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule deleted successfully")
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Get a schedule by ID", description = "Retrieve the details of a schedule by its ID")
    public ResponseData<ScheduleResponse> getScheduleById(@PathVariable("id") Long id) {
        log.info("Get schedule request for ID: {}", id);
        return ResponseData.<ScheduleResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Schedule retrieved successfully")
                .data(scheduleService.getScheduleById(id))
                .build();
    }


    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Get all schedules", description = "Retrieve a paginated list of schedules with optional filters")
    public ResponseData<PageResponse<ScheduleResponse>> getAllSchedules(
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam(value = "roomId", required = false) Integer roomId,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "date") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        log.info("Get all schedules request with doctorId: {}, roomId: {}, startDate: {}, endDate: {}, page: {}, size: {}, sort: {}, direction: {}",
                doctorId, roomId, startDate, endDate, page, size, sort, direction);
        return ResponseData.<PageResponse<ScheduleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Schedules retrieved successfully")
                .data(scheduleService.getAllSchedules(doctorId, roomId, startDate, endDate, page, size, sort, direction))
                .build();
    }

    @GetMapping("/available")
    @Operation(summary = "Get available schedules", description = "Retrieve a paginated list of available schedules with optional filters")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER', 'DOCTOR', 'NURSE')")
    public ResponseData<List<ScheduleInfoResponse>> getAvailableSchedules(
        @RequestParam(value = "medicalSpecialtyId") Integer medicalSpecialtyId,
        @RequestParam(value = "date") LocalDate date) {
        log.info("Get available schedules request with medicalSpecialtyId: {}, date: {}",
                medicalSpecialtyId, date);
        return ResponseData.<List<ScheduleInfoResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Available schedules retrieved successfully")
                .data(scheduleService.getAvailableSchedules(medicalSpecialtyId, date))
                .build();
    }
}